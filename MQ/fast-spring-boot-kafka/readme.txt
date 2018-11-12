一。Mac搭建Kafka：

===============================================================Kafka使用说明===============================================================
1、安装工具brew install kafka 会自动安装依赖zookeeper;（自动安装最新的zookeeper，如: 3.4.13）
2、安装配置文件位置 /usr/local/etc/kafka;(下面有kafka、zookeeper的配置文件)
3、启动kafka：zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties
	注意：a. 可以先切换到kafka安装目录，cd /usr/local/Cellar/kafka/2.0.0/libexec/bin/
		 b. 也可以先启动zookeeper，再启动kafka;
		 c. 如果已经启动zookeeper，需要先杀掉zookeeper进程;
4、创建topic：kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
5、查看创建的topic：kafka-topics --list --zookeeper localhost:2181
6、生产数据：kafka-console-producer --broker-list localhost:9092 --topic test
7、消费数据：kafka-console-consumer --bootstrap-server localhost:9092 --topic test --from-beginning


备注：
a. 如果是kafka早期版本，则应该使用--zookeeper localhost:2181替代--bootstrap-server localhost:9092;
	如：kafka-console-consumer --zookeeper localhost:2181 --topic test--from-beginning

参考链接：https://www.jianshu.com/p/cc540f29e779
==========================================================================================================================================


===============================================================集成到SpringBoot============================================================
1、进入https://start.spring.io/;（也可以：File -> New -> Project -> Spring Initializr）
2、SpringBoot选择2.0版本，再添加依赖：kafka;
3、生成项目即可;
4、项目中包含简单入门版本和高级配置版本;
5、运行结果：
Producer -> 消息发送成功：86ae89b4-e2fc-47f5-9f71-4e008370ca0b
Consumer -> 消息接收成功：86ae89b4-e2fc-47f5-9f71-4e008370ca0b
Producer -> 消息发送成功：008485cf-06bf-47a1-ad1c-91829ca843fc
Consumer -> 消息接收成功：008485cf-06bf-47a1-ad1c-91829ca843fc
Producer -> 消息发送成功：28866b4c-9349-449a-842f-3f9dce041ead
Consumer -> 消息接收成功：28866b4c-9349-449a-842f-3f9dce041ead
Consumer -> 消息接收成功：54af6615-8dda-49fd-ab1b-7acba907b9af
Producer -> 消息发送成功：54af6615-8dda-49fd-ab1b-7acba907b9af
Producer -> 消息发送成功：5f324f6d-0c58-48ab-8dba-2f2e3520a0d9
Consumer -> 消息接收成功：5f324f6d-0c58-48ab-8dba-2f2e3520a0d9
Producer -> 消息发送成功：cfb291ec-ad61-41c3-9623-e828ee63034b
Consumer -> 消息接收成功：cfb291ec-ad61-41c3-9623-e828ee63034b
Producer -> 消息发送成功：ee10d4b4-68e6-439d-94ad-f12d2f6fb1cc
Consumer -> 消息接收成功：ee10d4b4-68e6-439d-94ad-f12d2f6fb1cc
Consumer -> 消息接收成功：d8b3f927-1b83-426b-8846-8f5deb2dc037
Producer -> 消息发送成功：d8b3f927-1b83-426b-8846-8f5deb2dc037
Producer -> 消息发送成功：061bcac5-12f9-404b-b563-21ce49270b1a
Consumer -> 消息接收成功：061bcac5-12f9-404b-b563-21ce49270b1a
Producer -> 消息发送成功：00529e53-0050-4921-b208-751b44159fca
Consumer -> 消息接收成功：00529e53-0050-4921-b208-751b44159fca
Producer -> 消息发送成功：467d6a8a-730b-4b71-9e4f-a74af9e48432
==========================================================================================================================================
