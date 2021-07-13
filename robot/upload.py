import pymysql
import xlrd

# 连接数据库
try:
    db = pymysql.connect(host='localhost',user='root',passwd='123123',db='ruoyi',port=3306,charset='utf8')
except:
    print("无法连接数据库")


def open_excel():
    try:
        book = xlrd.open_workbook("3.xlsx")  # 文件名，把文件与py文件放在同一目录下
    except:
        print("打开文件失败")
    try:
        sheet = book.sheet_by_name("sheet1")  # execl里面的worksheet1
        return sheet
    except:
        print("定位失败")


def insert_deta():
    sheet = open_excel()
    cursor = db.cursor()
    row_num = sheet.nrows

    for i in range(0, row_num):  # 第一行是标题名，对应表中的字段名所以应该从第二行开始
        row_data = sheet.row_values(i)
        value = ('问题：'+row_data[0], row_data[1])

        print(i)
        sql = "INSERT INTO question(question,answer)VALUES(%s,%s)"#对应标题可以自由扩充
        cursor.execute(sql,value)  # 执行sql语句
        db.commit()
   # list.clear()  # 清空list
    cursor.close()  # 关闭连接
   # db.close()


open_excel()
insert_deta()