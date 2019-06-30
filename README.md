# GeoSearch
基于geohash算法的位置搜索，利用elasticsearch优化，同时mysql空间数据库引擎的测试

## 基于springboot2
目前很多资料都是springboot1，es的版本也比较老。es用的是6.2.3,没有用最新的es是因为最新的要装java11，不想折腾虚拟机了。

## 必要环境
java8、elasticsearch6.2.3

## 实现功能
1．	随机生成90万条经纬度信息

2．	利用Geohash算法存储数据点

3．	检索周围30米距离的数据点，用时5毫秒（第一次使用要重构索引，时间会长一点），检索出50条记录。

4．	利用jmeter模拟一万个客户端在线，每15秒刷新一次数据，Cpu负荷60%。

# 以下是mysql8的测试
mysql8空间数据库的函数名前面都加了ST，目前网上资料也不较少，经过一点波折也调通了。我的测试结果是mysql比es差很多。

## 1.创建经纬度表
```
CREATE TABLE `points` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '',
  `location` point NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`,`name`),
  SPATIAL KEY `sp_index` (`location`)
) ENGINE=MyISAM AUTO_INCREMENT=216060 DEFAULT CHARSET=gbk;
```
## 2.创建随机生成数据的func
```
CREATE DEFINER=`root`@`localhost` PROCEDURE `fill_points`(
IN size INT(10)
)
BEGIN
DECLARE i DOUBLE(10,1) DEFAULT size;
DECLARE lon FLOAT(7,4);
DECLARE lat FLOAT(6,4);
DECLARE position VARCHAR(100);
-- Deleting all.
DELETE FROM Points;
WHILE i > 0 DO
SET lon = RAND() * 360 - 180;
SET lat = RAND() * 180 - 90;
SET position = CONCAT( 'POINT(', lon, ' ', lat, ')' );
INSERT INTO Points(name, location) VALUES ( CONCAT('name_', i), ST_GeomFromtext(position) );
SET i = i - 1;
END WHILE;
END
```
## 随机生成一百万经纬度数据
```
CALL fill_points(1000000);
```

## 搜索附近
```
CREATE DEFINER=`root`@`localhost` PROCEDURE `fill_points`(
IN size INT(10)
)
BEGIN
DECLARE i DOUBLE(10,1) DEFAULT size;
DECLARE lon FLOAT(7,4);
DECLARE lat FLOAT(6,4);
DECLARE position VARCHAR(100);
-- Deleting all.
DELETE FROM Points;
WHILE i > 0 DO
SET lon = RAND() * 360 - 180;
SET lat = RAND() * 180 - 90;
SET position = CONCAT( 'POINT(', lon, ' ', lat, ')' );
INSERT INTO Points(name, location) VALUES ( CONCAT('name_', i), ST_GeomFromtext(position) );
SET i = i - 1;
END WHILE;
END
```



