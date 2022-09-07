### openGauss-tools-onlineMigration-mysql

#### 介绍
Mysql到openGauss的在线迁移方案的实现原理如下： 基于开源三方件mysql-binlog-connector-java解析mysql的binlog，并根据mysql主备并行复制的原理，对可并行的事务在openGauss端采用多线程进行同步回放，其中并行事务的判断规则为：如果所有正在回放的事务的最小sequence_number大于该事务的last_committed，那么该事务就可以并发执行。该方案可以严格保证事务的顺序和一致性。

#### 约束条件

- 仅支持从MySQL迁移至openGauss，支持DML迁移，不支持DDL迁移

- 为保证事务的顺序和一致性，不支持skip_event, limit_table, skip_table等设置

- MySQL5.7及以上版本

- MySQL参数设置： log_bin=ON, binlog_format=ROW, binlog_row_image=FULL, gtid_mode = ON

- 先进行离线复制，再进行在线复制，离线复制可基于[chameleon](https://gitee.com/opengauss/openGauss-tools-chameleon)工具完成

#### 使用教程

- 修改配置文件，配置文件的路径为：

  ```
  openGauss-tools-onlineMigration-mysql/mysql2openGauss/src/main/resources/config.yml
  ```

- 编译命令

  ```
  mvn clean package
  ```

- 运行命令

  ```
  java -jar ./target/online-migration-mysql-1.0-SNAPSHOT.jar
  ```

- 高性能运行命令

  Kunpeng-920 2p openEuler机器上

  ```
  numactl -C 0-31 -m 0 java -Xms15G -Xmx25G -jar ./target/online-migration-mysql-1.0-SNAPSHOT.jar
  ```

#### 迁移性能

- 利用sysbench对mysql进行压测，在10张表30个线程并发情况下，针对insert场景，在Kunpeng-920 2p openEuler机器上测试，整体在线迁移性能可达3w tps。