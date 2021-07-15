import csv  #用于把爬取的数据存储为csv格式，可以excel直接打开的
import time  #用于对请求加延时，爬取速度太快容易被反爬
from time import sleep #同上
import random  #用于对延时设置随机数，尽量模拟人的行为
import requests  #用于向网站发送请求
from lxml import etree    #lxml为第三方网页解析库，强大且速度快

for x in range(1,14):
    url = 'https://jiaowuchu.buct.edu.cn/614/list.htm'.format(x)
headers = {
    'User-Agent': "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36",
}
response = requests.get(url, headers=headers, timeout=10)
response.encoding = 'utf-8'
html = response.text

#print(html)

parse = etree.HTML(html)  #解析网页
all_div = parse.xpath('//*[@id="height2"]')

for li in all_div:
    li = {
        'question': ''.join('%s' %id for id in li.xpath('./div[2]/div/ul/li/a/text()'))+'\n'
        #'answer': ''.join('%s' % id for id in li.xpath('./div[2]/div[2]/div/p/span/span/text()'))


    }

    print(li)

    with open('Q.xlsx', 'a', encoding='utf_8_sig', newline='') as fp:
        # 'a'为追加模式（添加）
        # utf_8_sig格式导出csv不乱码
        fieldnames = ['question']
        writer = csv.DictWriter(fp, fieldnames)
        writer.writerow(li)

