import os
import time
import logging
from collections import deque
import xlrd
import xlwt
import jieba
import jieba.posseg as pseg
import warnings
warnings.filterwarnings('ignore')

from utils import (
    get_logger,
    similarity,
)


jieba.dt.tmp_dir = "./"
jieba.default_logger.setLevel(logging.ERROR)
logger = get_logger('faqrobot', logfile="faqrobot.log")

'''
import pymysql
#打开数据库连接
conn = pymysql.connect(host='localhost',user='root',passwd='123123',db='ruoyi')
print (conn)
print (type(conn))
'''

'''
-----------连接数据库导出excel-----------------
'''
import MySQLdb

conn=MySQLdb.connect(host='localhost',user='root',passwd='123123',db='ruoyi',port=3306,charset='utf8')
cursor=conn.cursor()
count = cursor.execute('select * from question')
print ('即将载入 %s 条数据' % count)
#重置游标位置
cursor.scroll(0,mode='absolute')
#搜取所有结果
results = cursor.fetchall()
#测试代码，print results
#获取MYSQL里的数据字段
fields = cursor.description
#将字段写入到EXCEL新表的第一行
wbk = xlwt.Workbook()
sheet = wbk.add_sheet('test1',cell_overwrite_ok=True)
for ifs in range(0,len(fields)): #列
    sheet.write(0,ifs,fields[ifs][0])
ics=1
jcs=0
for ics in range(1,len(results)+1): #行
    for jcs in range(0,len(fields)):
        sheet.write(ics-1,jcs,results[ics-1][jcs])

wbk.save('问答库.xlsx')

'''
-----------excel转为txt格式-----------------
'''

def strs(row):
    values = "";
    for i in range(len(row)):
        if i == len(row) - 1:
            values = values + str(row[i])
        else:
            values = values + str(row[i]) + "\n"
    return values

# 打卡文件
data = xlrd.open_workbook("问答库.xlsx")
sqlfile = open("问答库.txt", "w") # 文件读写方式是追加a,只写W

table = data.sheets()[0] # 表头
nrows = table.nrows  # 行数
ncols = table.ncols  # 列数
colnames = table.row_values(0)  # 某一行数据
# 打印出行数列数
#print(nrows)
#print(ncols)
#print(colnames)
for ronum in range(0, nrows): #从第一行开始转换
    row = table.row_values(ronum)
    values = strs(row) # 将行数据拼接成字符串

    sqlfile.writelines(values + "\r") #将字符串写入新文件
sqlfile.close() # 关闭写入的文件

'''
-----------开始识别文档-----------------
'''
class zhishiku(object):
    def __init__(self, q):  # a是答案（必须是1给）, q是问题（1个或多个）
        self.q = [q]
        self.a = ""
        self.sim = 0
        self.q_vec = []
        self.q_word = []

    def __str__(self):
        return 'q=' + str(self.q) + '\na=' + str(self.a) + '\nq_word=' + str(self.q_word) + '\nq_vec=' + str(self.q_vec)
        # return 'a=' + str(self.a) + '\nq=' + str(self.q)


class FAQrobot(object):
    def __init__(self, zhishitxt='问答库.txt', lastTxtLen=10, usedVec=False):
        # usedVec 如果是True 在初始化时会解析词向量，加快计算句子相似度的速度
        self.lastTxt = deque([], lastTxtLen)
       # print(self.lastTxt)
        self.zhishitxt = zhishitxt
      #  print(self.zhishitxt)
        self.usedVec = usedVec
      #  print(self.usedVec)
        self.reload()

    def load_qa(self):
        print('问答知识库开始载入')
        self.zhishiku = []
        with open(self.zhishitxt) as f:
            txt = f.readlines()
            abovetxt = 0    # 上一行的种类： 0空白/注释  1答案   2问题
            for t in txt:   # 读取FAQ文本文件
                t = t.strip()  #消除空格
                if not t or t.startswith('#'):
                    abovetxt = 0
                elif abovetxt != 2:
                    if t.startswith('问题：'): # 输入第一个问题
                        self.zhishiku.append(zhishiku(t[3:])) #3-消除问题头
                        abovetxt = 2
                        #print(t)
                        #print(zhishiku(t[3:]))
                    else:       # 输入答案文本（非第一行的）
                        self.zhishiku[-1].a += '\n' + t
                        abovetxt = 1
                else:
                    if t.startswith('问题：'): # 输入问题（非第一行的）
                        self.zhishiku[-1].q.append(t[3:])
                        abovetxt = 2
                        #print(zhishiku(t[3:]))
                    else:       # 输入答案文本
                        self.zhishiku[-1].a += t
                        abovetxt = 1

        for t in self.zhishiku: #jiebba分词
            for question in t.q:
                t.q_word.append(set(jieba.cut(question)))
                #print(t.q_word)

    def load_embedding(self):
        from gensim.models import Word2Vec
        if not os.path.exists('Word60.model'):
            self.vecModel = None
            return

        # 载入60维的词向量(Word60.model，Word60.model.syn0.npy，Word60.model.syn1neg.npy）
        self.vecModel = Word2Vec.load('Word60.model')
        for t in self.zhishiku:
            t.q_vec = []
            for question in t.q_word:
                t.q_vec.append({t for t in question if t in self.vecModel.index2word})


    def reload(self):
        self.load_qa()
        self.load_embedding()

        print('问答知识库载入完毕')

    def maxSimTxt(self, intxt, simCondision=0.1, simType='simple'):
        """
        找出知识库里的和输入句子相似度最高的句子
        simType=simple, simple_POS, vec
        simple：简单的对比相同词汇数量，得到句子相似度
        simple_POS：简单的对比相同词汇数量,并对词性乘以不同的权重，得到句子相似度
        vec：用词向量计算相似度,并对词性乘以不同的权重，得到句子相似度
        all：调试模式，把以上几种模式的结果都显示出来，方便对比和调试
        """
        #result = jieba.tokenize(intxt, mode='search')
        #for tk in result:
         #   print("word %s\t\t start: %d \t\t end:%d" % (tk[0], tk[1], tk[2]))

        self.lastTxt.append(intxt) #intxt是文本输入
        if simType not in ('simple', 'simple_pos', 'vec'):
            return 'error:  maxSimTxt的simType类型不存在: {}'.format(simType)

        # 如果没有加载词向量，那么降级成 simple_pos 方法
        embedding = self.vecModel
        if simType == 'vec' and not embedding:
            simType = 'simple_pos'

        for t in self.zhishiku:
            questions = t.q_vec if simType == 'vec' else t.q_word
            in_vec = jieba.lcut(intxt) if simType == 'simple' else pseg.lcut(intxt)

            t.sim = max(
                similarity(in_vec, question, method=simType, embedding=embedding)
                for question in questions
            )
        maxSim = max(self.zhishiku, key=lambda x: x.sim)

        logger.info('maxSim=' + format(maxSim.sim, '.0%'))

        if maxSim.sim < simCondision:

           # print('您是否想问: %s' % t.q)
            return '抱歉，我没有理解您的意思。'

        if 0.1 < maxSim.sim < 0.4 :
            print('您是否想问: %s' % maxSim.q)
            #return'您是否想问: %s' % t.q

        return maxSim.a

    def answer(self, intxt, simType='simple'):
        """simType=simple, simple_POS, vec, all"""
        if not intxt:
            return ''

        if simType == 'all':  # 用于测试不同类型方法的准确度，返回空文本
            for method in ('simple', 'simple_pos', 'vec'):
                outtext = 'method:\t' + self.maxSim(intxt, simType=method)
                print(outtext)

            return ''
        else:
            outtxt = self.maxSimTxt(intxt, simType=simType)
            # 输出回复内容，并计入日志
        return outtxt


if __name__ == '__main__':
    robot = FAQrobot('问答库.txt', usedVec=False)
    print('你好，我是智能机器人^-^')
    while True:
        # simType=simple, simple_pos, vec, all
        print('回复：' + robot.answer(input('输入：'), 'simple_pos') + '\n')
