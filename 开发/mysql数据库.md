# mysql数据库

## 1、对数据库的操作

### 1.1、创建数据库

```sql
CREATE DATABASE database-name
```



### 1.2、删除数据库

```sql
DROP DATABASE database-name
```

## 2、对表的操作

### 2.1、单表操作

- 建表

  ```sql
  create table users(
    id int(10) not null primary key,
    name varchar(255) not null,
    pwd varchar(255) not null
  );
  ```

- 插入数据

  ```sql
  insert into users values(1，'张三'，'123456'),(2,'李四','123456')
  ```

- 在表中添加字段

  ```sql
  alter table student add column phone varchar(11) not null comment'电话号码';
  ```

- 添加字段内的值

  ```sql
   update student set phone='13121312131' where id=1;
  ```

  ​

- 修改中文列的编码格式

  ```sql
  //修改单项
  ALTER TABLE users CHANGE name name VARCHAR(255) CHARACTER SET utf8;
  ALTER TABLE users CHANGE pwd pwd VARCHAR(255) CHARACTER SET utf8;
  //修改数据库的编码格式
  ALTER DATABASE mybatis CHARACTER SET=utf8;
  ```

- 检索字段名并升序排列

  ```sql
  mysql> select name ,age from student order by age;
  +------+-----+
  | name | age |
  +------+-----+
  | 张飞 | 18  |
  | 关羽 | 21  |
  +------+-----+
  2 rows in set (0.00 sec)
  ```

- 检索字段名并按降序排列

  ```sql
  mysql> select name ,age from student order by age desc;
  +------+-----+
  | name | age |
  +------+-----+
  | 关羽 | 21  |
  | 张飞 | 18  |
  +------+-----+
  2 rows in set (0.00 sec)
  ```

- 检索字段名并绛序只允许显示3条

  ```sql
  mysql> select name ,age from student order by age desc limit 1;
  +------+-----+
  | name | age |
  +------+-----+
  | 关羽 | 21  |
  +------+-----+
  1 row in set (0.00 sec)
  ```

- 检索案例表里条件age字段为21或24的所有内容按age升序排列

  ```sql
  mysql> select name,age,address from student where age='21'or age='18' order by age;
  +------+-----+---------+
  | name | age | address |
  +------+-----+---------+
  | 张飞 | 18  | 武汉    |
  | 赵云 | 18  | 咸宁    |
  | 关羽 | 21  | 宜昌    |
  +------+-----+---------+
  3 rows in set (0.00 sec)

  ```

- 检索案例表中address为咸宁和孝感，并且年龄小于等于19的所有信息并按年龄升序排列

  ```sql
  mysql> select * from student where (address='咸宁' or address='孝感') and age <= '19' order by age;
  +----+------+---------+-------------+-----+------------+
  | id | name | address | phone       | age | birthday   |
  +----+------+---------+-------------+-----+------------+
  |  3 | 赵云 | 咸宁    | 12345678912 | 18  | 1998-01-21 |
  |  6 | 赵云 | 咸宁    | 12345678912 | 18  | 1998-01-21 |
  |  4 | 刘备 | 孝感    | 15915915912 | 19  | 1997-09-07 |
  |  5 | 刘备 | 孝感    | 15915915912 | 19  | 1997-09-07 |
  +----+------+---------+-------------+-----+------------+
  4 rows in set (0.01 sec)
  ```

- 检索案例表里条件age字段不是21和23的name,age,address信息

  ```sql
  mysql> select name,address,age from student where age not in ('18','19')
      -> ;
  +------+---------+-----+
  | name | address | age |
  +------+---------+-----+
  | 关羽 | 宜昌    | 21  |
  +------+---------+-----+
  1 row in set (0.00 sec)
  ```

- 检索案例条件birthday字段为’1997-09-01’和’2000-01-01’的name,birthday的内容按升序排列

  ```sql
  mysql> select name,address,birthday from student where birthday between '1997-09-01' and '2000-01-01' order by birthday;
  +------+---------+------------+
  | name | address | birthday   |
  +------+---------+------------+
  | 刘备 | 孝感    | 1997-09-07 |
  | 刘备 | 孝感    | 1997-09-07 |
  | 赵云 | 咸宁    | 1998-01-21 |
  | 赵云 | 咸宁    | 1998-01-21 |
  | 张飞 | 武汉    | 1998-07-01 |
  +------+---------+------------+
  5 rows in set (0.00 sec)
  ```

- 检索案例age不等于19的所有信息并案绛序排列

  ```sql
  mysql> select name,address,age from student where age!='19' order by age desc;
  +------+---------+-----+
  | name | address | age |
  +------+---------+-----+
  | 关羽 | 宜昌    | 21  |
  | 张飞 | 武汉    | 18  |
  | 赵云 | 咸宁    | 18  |
  | 赵云 | 咸宁    | 18  |
  +------+---------+-----+
  ```

- 模糊查询：检索案例表里搜索条件name字段为张…的所有内容

  ```sql
  /*两种方法*/
  mysql> select * from student where name like '张%';
  +----+------+---------+-------------+-----+------------+
  | id | name | address | phone       | age | birthday   |
  +----+------+---------+-------------+-----+------------+
  |  1 | 张飞 | 武汉    | 13121312131 | 18  | 1998-07-01 |
  +----+------+---------+-------------+-----+------------+
  1 row in set (0.00 sec)

  mysql> select * from student where name like '张_';
  +----+------+---------+-------------+-----+------------+
  | id | name | address | phone       | age | birthday   |
  +----+------+---------+-------------+-----+------------+
  |  1 | 张飞 | 武汉    | 13121312131 | 18  | 1998-07-01 |
  +----+------+---------+-------------+-----+------------+
  1 row in set (0.00 sec)
  ```

- 检索案例从表里搜索name字段为…云…的所有信息

  ```sql
  /*同上，有两种方法*/
  mysql> select * from student where name like '%云%' ;
  +----+------+---------+-------------+-----+------------+
  | id | name | address | phone       | age | birthday   |
  +----+------+---------+-------------+-----+------------+
  |  3 | 赵云 | 咸宁    | 12345678912 | 18  | 1998-01-21 |
  |  6 | 赵云 | 咸宁    | 12345678912 | 18  | 1998-01-21 |
  +----+------+---------+-------------+-----+------------+
  2 rows in set (0.00 sec)
  ```

- 检索案例表里email字段带有…qq.com的显示name,email所有内容

  ```sql
  mysql> select name,email from student where email regexp'qq.com$';
  +------+--------------+
  | name | email        |
  +------+--------------+
  | 张飞 | 15465@qq.com |
  | 关羽 | 4564@qq.com  |
  +------+--------------+
  2 rows in set (0.00 sec)
  ```

- 检索案例表里email字段除了…qq.com的所有内容

  ```sql
  /*两种方法*/
  mysql> select * from student where email regexp '\w*[^qq$].com';
  +----+------+---------+-------------+-----+------------+-----------+
  | id | name | address | phone       | age | birthday   | email     |
  +----+------+---------+-------------+-----+------------+-----------+
  |  3 | 赵云 | 咸宁    | 12345678912 | 18  | 1998-01-21 | 4564@.com |
  |  4 | 刘备 | 孝感    | 15915915912 | 19  | 1997-09-07 | 4888@.com |
  |  5 | 刘备 | 孝感    | 15915915912 | 19  | 1997-09-07 | 4999@.com |
  |  6 | 赵云 | 咸宁    | 12345678912 | 18  | 1998-01-21 | 4000@.com |
  +----+------+---------+-------------+-----+------------+-----------+
  4 rows in set (0.00 sec)

  mysql> select * from student where email regexp'.*[^qq$].com';
  +----+------+---------+-------------+-----+------------+-----------+
  | id | name | address | phone       | age | birthday   | email     |
  +----+------+---------+-------------+-----+------------+-----------+
  |  3 | 赵云 | 咸宁    | 12345678912 | 18  | 1998-01-21 | 4564@.com |
  |  4 | 刘备 | 孝感    | 15915915912 | 19  | 1997-09-07 | 4888@.com |
  |  5 | 刘备 | 孝感    | 15915915912 | 19  | 1997-09-07 | 4999@.com |
  |  6 | 赵云 | 咸宁    | 12345678912 | 18  | 1998-01-21 | 4000@.com |
  +----+------+---------+-------------+-----+------------+-----------+
  4 rows in set (0.00 sec)

  ```

- 检索案例表中总人数，最小年龄，最大年龄，年龄总和，平均值并起别名；

  ```sql
  mysql> select count(*) as 总人数,min(age) as 最小年龄,sum(age) as 总和,avg(age) as 平均值 from student;
  +--------+----------+------+--------------------+
  | 总人数 | 最小年龄 | 总和 | 平均值             |
  +--------+----------+------+--------------------+
  |      6 | 18       |  113 | 18.833333333333332 |
  +--------+----------+------+--------------------+
  1 row in set (0.01 sec)

  ```

- 检索表里age年龄最大的，并只显示一位的信息；

  ```sql
  mysql> select * from student order by age desc limit 1;
  +----+------+---------+-------+-----+------------+-------------+
  | id | name | address | phone | age | birthday   | email       |
  +----+------+---------+-------+-----+------------+------------	-+
  |  2 | 关羽 | 宜昌    |       | 21  | 1996-07-01 | 4564@qq.com |
  +----+------+---------+-------+-----+------------+-------------+
  1 row in set (0.00 sec)
  ```

- 时间处理的相关函数

  ```sql
  mysql> select curtime();
  +-----------+
  | curtime() |
  +-----------+
  | 17:02:51  |
  +-----------+
  1 row in set (0.01 sec)

  mysql> select curtime();
  +-----------+
  | curtime() |
  +-----------+
  | 17:02:53  |
  +-----------+
  1 row in set (0.00 sec)

  mysql> select curtime();
  +-----------+
  | curtime() |
  +-----------+
  | 17:02:54  |
  +-----------+
  1 row in set (0.00 sec)

  mysql> select curtime();
  +-----------+
  | curtime() |
  +-----------+
  | 17:02:55  |
  +-----------+
  1 row in set (0.00 sec)

  mysql> select curdate();
  +------------+
  | curdate()  |
  +------------+
  | 2022-01-05 |
  +------------+
  1 row in set (0.00 sec)

  mysql> select month(now());
  +--------------+
  | month(now()) |
  +--------------+
  |            1 |
  +--------------+
  1 row in set (0.02 sec)

  mysql> select now();
  +---------------------+
  | now()               |
  +---------------------+
  | 2022-01-05 17:04:12 |
  +---------------------+
  1 row in set (0.00 sec)

  ```

- 检索出生日是7月份的学生同学

  ```sql
  mysql> select name,birthday from student where month(birthday)=7;
  +------+------------+
  | name | birthday   |
  +------+------------+
  | 张飞 | 1998-07-01 |
  | 关羽 | 1996-07-01 |
  +------+------------+
  2 rows in set (0.00 sec)
  ```

- 检索出生日期在下半年的同学并按月份排序

  ```sql
  mysql> select * from student where month(birthday)>=6 and month(birthday)<=12 order by month(birthday);
  +----+------+---------+-------------+-----+------------+--------------+
  | id | name | address | phone       | age | birthday   | email        |
  +----+------+---------+-------------+-----+------------+--------------+
  |  1 | 张飞 | 武汉    | 13121312131 | 18  | 1998-07-01 | 15465@qq.com |
  |  2 | 关羽 | 宜昌    |             | 21  | 1996-07-01 | 4564@qq.com  |
  |  4 | 刘备 | 孝感    | 15915915912 | 19  | 1997-09-07 | 4888@.com    |
  |  5 | 刘备 | 孝感    | 15915915912 | 19  | 1997-09-07 | 4999@.com    |
  +----+------+---------+-------------+-----+------------+--------------+
  4 rows in set (0.00 sec)
  ```

- 检索所有学生的姓

  ```sql
  mysql> select left(name,1),name from student;
  +--------------+------+
  | left(name,1) | name |
  +--------------+------+
  | 张           | 张飞 |
  | 关           | 关羽 |
  | 赵           | 赵云 |
  | 刘           | 刘备 |
  | 刘           | 刘备 |
  | 赵           | 赵云 |
  +--------------+------+
  6 rows in set (0.02 sec)
  ```

- 检索年龄并去重

  ```sql
  mysql> select distinct(age) from student order by age;
  +-----+
  | age |
  +-----+
  | 18  |
  | 19  |
  | 21  |
  +-----+
  3 rows in set (0.00 sec)
  ```

- 检索表里年龄有几个并查看内容

  ```sql
  mysql> select count(age) as 年龄数,age from student group by age having max(age);
  +--------+-----+
  | 年龄数 | age |
  +--------+-----+
  |      3 | 18  |
  |      2 | 19  |
  |      1 | 21  |
  +--------+-----+
  3 rows in set (0.00 sec)
  ```

- 检索表里年龄有几个年龄等于18并查看内容

  ```sql
  mysql> select count(age) as 年龄数,age from student group by age having max(age)=18;
  +--------+-----+
  | 年龄数 | age |
  +--------+-----+
  |      3 | 18  |
  +--------+-----+
  1 row in set (0.00 sec)
  ```

  ​


### 2.2、多表操作