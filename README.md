# GeoSearch
基于geohash算法的位置搜索，利用elasticsearch优化，同时mysql空间数据库引擎的测试

## 基于springboot2
目前很多资料都是springboot1，es的版本也比较老。es用的是6.2.3,没有用最新的es是因为最新的要装java11，不想折腾虚拟机了。

## 必要环境
java8、elasticsearch6.2.3

## 实现功能
1．	随机生成90万条经纬度信息
2．	利用Geohash算法存储数据点
3．	检索周围30米距离的数据点，用时5毫秒，检索出50条记录。
4．	利用jmeter模拟一万个客户端在线，每15秒刷新一次数据，Cpu负荷60%。



