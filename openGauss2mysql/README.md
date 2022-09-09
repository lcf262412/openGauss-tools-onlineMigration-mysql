### openGauss-tools-reverseMigration-mysql

#### 介绍
openGauss到mysql的在线迁移方案的实现原理如下： 在openGauss端开启逻辑复制，使用jdbc获取到逻辑解码，对逻辑解码进行sql解析，通过多线程并发迁移到mysql端。

#### 约束条件

- 仅支持从openGauss迁移至mysql，支持DML迁移，不支持DDL迁移
- MySQL5.7及以上版本，openGauss支持逻辑复制
- 迁移前需要开启数据库的逻辑复制相关配置：wal_level = logical，无主键的表需要执行：alter table tablename replica identity full；

#### 使用教程

- 修改配置文件，配置文件的路径为：

  ```
  openGauss-tools-reverseMigration-mysql/config.yml
  ```

- 编译命令

  ```
  mvn clean package
  ```

- 运行命令

  ```
  java -jar ./target/reverse-migration-mysql-1.0-SNAPSHOT.jar start/create/drop
  ```

- 高性能运行命令

  Kunpeng-920 2p openEuler机器上

  ```
  numactl -C 0-31 -m 0 java -Xms15G -Xmx25G -jar ./target/reverse-migration-mysql-1.0-SNAPSHOT.jar
  ```

#### 迁移性能

- 利用sysbench对mysql进行压测，在100张表100个线程并发情况下，针对insert场景，在蓝区虚拟机上测试，整体在线迁移性能可达1w tps。