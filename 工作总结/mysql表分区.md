

## mysql 表分区

我们使用mysql时，如果单表数据库达到千万条以上时，效率会下降，我们需要做相应的优化操作。分区是针对这种情况的一种相对比较简单实用的方式。

[TOC]

##### 分区的好处是：

-  可以让单表存储更多的数据
-  分区表的数据更容易维护，可以通过清楚整个分区批量删除大量数据，也可以增加新的分区来支持新插入的数据。另外，还可以对一个独立分区进行优化、检查、修复等操作
-  部分查询能够从查询条件确定只落在少数分区上，速度会很快
-  分区表的数据还可以分布在不同的物理设备上，从而高效利用多个硬件设备
-  可以使用分区表赖避免某些特殊瓶颈，例如InnoDB单个索引的互斥访问、ext3文件系统的inode锁竞争
-  可以备份和恢复单个分区

#####  分区的限制和缺点：

-  一个表最多只能有1024个分区
-  如果分区字段中有主键或者唯一索引的列，那么所有主键列和唯一索引列都必须包含进来
-  分区表无法使用外键约束
-  NULL值会使分区过滤无效
-  所有分区必须使用相同的存储引擎

##### 分区的类型：

-  RANGE分区：基于属于一个给定连续区间的列值，把多行分配给分区
-  LIST分区：类似于按RANGE分区，区别在于LIST分区是基于列值匹配一个离散值集合中的某个值来进行选择
-  HASH分区：基于用户定义的表达式的返回值来进行选择的分区，该表达式使用将要插入到表中的这些行的列值进行计算。这个函数可以包含MySQL中有效的、产生非负整数值的任何表达式
-  KEY分区：类似于按HASH分区，区别在于KEY分区只支持计算一列或多列，且MySQL服务器提供其自身的哈希函数。必须有一列或多列包含整数值

###### RANGE分区

基于属于一个给定连续区间的列值，把多行分配给分区。这些区间要连续且不能相互重叠，使用VALUES LESS THAN操作符来进行定义。以下是实例。

```sql
CREATE TABLE employees (
    id INT NOT NULL,
    fname VARCHAR(30),
    lname VARCHAR(30),
    hired DATE NOT NULL DEFAULT '1970-01-01',
    separated DATE NOT NULL DEFAULT '9999-12-31',
    job_code INT NOT NULL,
    store_id INT NOT NULL
)  
 PARTITION BY RANGE (store_id) (
    PARTITION p0 VALUES LESS THAN (6),
    PARTITION p1 VALUES LESS THAN (11),
    PARTITION p2 VALUES LESS THAN (16),
    PARTITION p3 VALUES LESS THAN MAXVALUE
)；
```

MAXVALUE 表示最大的可能的整数值。现在，store_id 列值大于或等于16（定义了的最高值）的所有行都将保存在分区p3中。在将来的某个时候，当商店数已经增长到25, 30, 或更多 ，可以使用ALTER TABLE语句为商店21-25, 26-30,等等增加新的分区。

**RANGE分区在如下场合特别有用：**
      1）、 当需要删除一个分区上的“旧的”数据时,只删除分区即可。对于有大量行的表，这比运行一个如“DELETE FROM employees WHERE YEAR (separated) <= 1990；”这样的一个DELETE查询要有效得多。
      2）、想要使用一个包含有日期或时间值，或包含有从一些其他级数开始增长的值的列。
      3）、经常运行直接依赖于用于分割表的列的查询。例如，当执行一个如“SELECT COUNT(*) FROM employees WHERE YEAR(separated) = 2000 GROUP BY store_id；”这样的查询时，MySQL可以很迅速地确定只有分区p2需要扫描，这是因为余下的分区不可能包含有符合该WHERE子句的任何记录。



###### LIST分区

类似于按RANGE分区，区别在于LIST分区是基于列值匹配一个离散值集合中的某个值来进行选择。
      LIST分区通过使用“PARTITION BY LIST(expr)”来实现，其中“expr” 是某列值或一个基于某个列值、并返回一个整数值的表达式，然后通过“VALUES IN (value_list)”的方式来定义每个分区，其中“value_list”是一个通过逗号分隔的整数列表。
注释：在MySQL 5.1中，当使用LIST分区时，有可能只能匹配整数列表。

要按照属于同一个地区商店的行保存在同一个分区中的方式来分割表，可以使用下面的“CREATE TABLE”语句：

```sql
CREATE TABLE employees (
    id INT NOT NULL,
    fname VARCHAR(30),
    lname VARCHAR(30),
    hired DATE NOT NULL DEFAULT '1970-01-01',
    separated DATE NOT NULL DEFAULT '9999-12-31',
    job_code INT,
    store_id INT
)  
 PARTITION BY LIST(store_id)
    PARTITION pNorth VALUES IN (3,5,6,9,17),
    PARTITION pEast VALUES IN (1,2,10,11,19,20),
    PARTITION pWest VALUES IN (4,12,13,14,18),
    PARTITION pCentral VALUES IN (7,8,15,16)
)；
```

这使得在表中增加或删除指定地区的雇员记录变得容易起来。例如，假定西区的所有音像店都卖给了其他公司。那么与在西区音像店工作雇员相关的所有记录（行）可以使用查询“ALTER TABLE employees DROP PARTITION pWest；”来进行删除，它与具有同样作用的DELETE （删除）查询“DELETE query DELETE FROM employees WHERE store_id IN (4,12,13,14,18)；”比起来，要有效得多。





###### HASH分区      

 基于用户定义的表达式的返回值来进行选择的分区，该表达式使用将要插入到表中的这些行的列值进行计算。这个函数可以包含MySQL 中有效的、产生非负整数值的任何表达式。
      要使用HASH分区来分割一个表，要在CREATE TABLE 语句上添加一个“PARTITION BY HASH (expr)”子句，其中“expr”是一个返回一个整数的表达式。它可以仅仅是字段类型为MySQL 整型的一列的名字。此外，你很可能需要在后面再添加一个“PARTITIONS num”子句，其中num 是一个非负的整数，它表示表将要被分割成分区的数量。

```sql
CREATE TABLE employees (
    id INT NOT NULL,
    fname VARCHAR(30),
    lname VARCHAR(30),
    hired DATE NOT NULL DEFAULT '1970-01-01',
    separated DATE NOT NULL DEFAULT '9999-12-31',
    job_code INT,
    store_id INT
)
PARTITION BY HASH(store_id)
PARTITIONS 4；
```

如果没有包括一个PARTITIONS子句，那么分区的数量将默认为1。 例外： 对于NDB Cluster（簇）表，默认的分区数量将与簇数据节点的数量相同，
这种修正可能是考虑任何MAX_ROWS 设置，以便确保所有的行都能合适地插入到分区中。
1.)LINER HASH
MySQL还支持线性哈希功能，它与常规哈希的区别在于，线性哈希功能使用的一个线性的2的幂（powers-of-two）运算法则，而常规 哈希使用的是求哈希函数值的模数。
线性哈希分区和常规哈希分区在语法上的唯一区别在于，在“PARTITION BY” 子句中添加“LINEAR”关键字。

```sql
CREATE TABLE employees (
    id INT NOT NULL,
    fname VARCHAR(30),
    lname VARCHAR(30),
    hired DATE NOT NULL DEFAULT '1970-01-01',
    separated DATE NOT NULL DEFAULT '9999-12-31',
    job_code INT,
    store_id INT
)
PARTITION BY LINEAR HASH(YEAR(hired))
PARTITIONS 4；
```

按照线性哈希分区的优点在于增加、删除、合并和拆分分区将变得更加快捷，有利于处理含有极其大量（1000吉）数据的表。它的缺点在于，与使用
常规HASH分区得到的数据分布相比，各个分区间数据的分布不大可能均衡。



###### KEY分区

类似于按HASH分区，区别在于KEY分区只支持计算一列或多列，且MySQL 服务器提供其自身的哈希函数。必须有一列或多列包含整数值。

```sql
CREATE TABLE tk (
    col1 INT NOT NULL,
    col2 CHAR(5),
    col3 DATE
)
PARTITION BY LINEAR KEY (col1)
PARTITIONS 3;
```

在KEY分区中使用关键字LINEAR和在HASH分区中使用具有同样的作用，分区的编号是通过2的幂（powers-of-two）算法得到，而不是通过模数算法。

------

##### 建立分区表

```sql
CREATE TABLE sensor_data_list(
sale_date  DATETIME NOT NULL,
ale_item  VARCHAR(2) NOT NULL,
sale_money DECIMAL(10,2) NOT NULL
)
PARTITION BY RANGE ((year(sale_date)*100+month(sale_date))*100+day(sale_date)) (
PARTITION s20100401 VALUES LESS THAN (20100402),
PARTITION s20100402 VALUES LESS THAN (20100403),
PARTITION s20100403 VALUES LESS THAN (20100404),
PARTITION s20100404 VALUES LESS THAN (20100405),
PARTITION s20100405 VALUES LESS THAN (20100406),
PARTITION s20100406 VALUES LESS THAN (20100407),
PARTITION p1 VALUES LESS THAN (MAXVALUE)
);
```



##### 添加分区

将用于分区的列添加为复合主键

添加复合主键成功后，在用命令创建分区：

```sql
ALTER TABLE sensor_data_list PARTITION BY RANGE (TO_DAYS(create_time))
(
PARTITION p_20180101 VALUES LESS THAN (TO_DAYS('2018-01-01')),
PARTITION p_20180201 VALUES LESS THAN (TO_DAYS('2018-02-01')),
PARTITION p_20180301 VALUES LESS THAN (TO_DAYS('2018-03-01')),
PARTITION p_20180401 VALUES LESS THAN (TO_DAYS('2018-04-01')),
PARTITION p_max VALUES LESS THAN MAXVALUE );
```



##### 增加分区

PARTITION p_max  VALUES LESS THAN (MAXVALUE) 这句要去掉，才可以增加分区

```sql
ALTER TABLE sensor_data_list 
 ADD PARTITION (PARTITION p_20180501 VALUES LESS THAN (TO_DAYS('2018-05-01')));  
```



##### 查看表分区

```sql
SELECT
      partition_name part, 
      partition_expression expr, 
      partition_description descr, 
      FROM_DAYS(partition_description) lessthan_sendtime, 
      table_rows 
    FROM
      INFORMATION_SCHEMA.partitions 
    WHERE
      TABLE_SCHEMA = SCHEMA() 
      AND TABLE_NAME='sensor_data_list'; ---这里是表名
```

**查看分区数据量，查看全库数据量**

```sql
USE information_schema;

SELECT PARTITION_NAME,TABLE_ROWS
FROM INFORMATION_SCHEMA.PARTITIONS
WHERE TABLE_NAME = 'sensor_data_list';



SELECT table_name,table_rows FROM TABLES 
WHERE TABLE_SCHEMA = 'db_name' 
ORDER BY table_rows DESC
```


;

##### 删除分区

删除分区即删除数据。

```sql
alter table test_rpt_office_lvl drop PARTITION trol_201711,trol_201712,trol_201801;
```



###### 利用存储过程自动创建表分区

 以下是按月分区

```mysql
SET FOREIGN_KEY_CHECKS=0;

-- Procedure structure for SP_TABLE_PARTITION_AUTO

DROP PROCEDURE IF EXISTS SP_TABLE_PARTITION_AUTO;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `SP_TABLE_PARTITION_AUTO`(tableName varchar(30),dataDate varchar(7),tempName varchar(10))
BEGIN

自动给表建立分区,建下个月的分区

tableName：表名 dataDate：数据日期   tempName：分区前缀  

  set @curYear = SUBSTR(dataDate,1,4);

set @curMonth = SUBSTR(dataDate,6,2);

set @parYear = '';

set @parMonth = '';

if @curMonth = '11' then 
		set @parYear = @curYear +1;
		set @parMonth = '01';
elseif @curMonth = '12' then 
		set @parYear = @curYear +1;
		set @parMonth = '02';
ELSE
		set @parYear = @curYear;
		set @parMonth = @curMonth +2;
		if @parMonth < 10 THEN
			 set @parMonth = CONCAT('0',@parMonth);
		end if;
end if;

if @curMonth = '12' then 
		set @curYear = @curYear +1;
		set @curMonth = '01';
ELSE
		set @curMonth = @curMonth +1;
		if @curMonth < 10 THEN
			 set @curMonth = CONCAT('0',@curMonth);
		end if;
end if;

#select @curYear as 'curYear',@curMonth as 'curMonth',@parYear as 'parYear',@parMonth as 'parMonth';

set @sqlStr = CONCAT('ALTER TABLE ',tableName,' add PARTITION ( PARTITION ',tempName,'_',@curYear,@curMonth,
		' VALUES LESS THAN (\'',@parYear,'-',@parMonth,'-','01','\'))');

PREPARE stmt FROM @sqlStr;  
EXECUTE stmt; 

END
;;

DELIMITER ;
```

