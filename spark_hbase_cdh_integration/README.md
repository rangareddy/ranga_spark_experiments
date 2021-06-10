# Spark HBase CDH Integration

## Create and Insert the data to `employees` table in HBase

### Login to HBase Shell
```sh
hbase shell
```

### Create the HBase table(s) using following commands:
```sh
create 'employee', 'per', 'prof'
```

### Insert the data
```sh
put 'employee','1','per:name','Ranga'
put 'employee','1','per:age','32'
put 'employee','1','prof:designation','Software Engineer'
put 'employee','1','prof:salary','60000'

put 'employee','2','per:name','Nishanth'
put 'employee','2','per:age','3'
put 'employee','2','prof:designation','Junior Software Engineer'
put 'employee','2','prof:salary','80000'
```

## Create the application deployment directory in spark gateway node. for example `/apps/spark/spark-hbase/`.
```sh
$ ssh username@node2.host.com
$ mkdir -p /apps/spark/spark-hbase/
$ chmod 755 /apps/spark/spark-hbase/
```

## Download the `spark_hbase_cdh_integration` project.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark_hbase_cdh_integration
```

### Build the `spark_hbase_cdh_integration` application.

> Before building the project update your spark cdh version according to your cluster version.

```sh
$ mvn clean package -DskipTests
```

### Copy the `spark_hbase_cdh_integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_hbase_cdh_integration.sh` to spark gateway node `/apps/spark/spark-hbase/` directory.
```sh
$ scp target/spark_hbase_cdh_integration-1.0.0-SNAPSHOT.jar root@node2.host.com:/apps/spark/spark-hbase/
$ scp run_spark_hbase_cdh_integration.sh root@node2.host.com:/apps/spark/spark-hbase/
```

### Login to gateway node and run the `run_spark_hbase_cdh_integration.sh` shell script.
```sh
sh /apps/spark/spark-hbase/run_spark_hbase_cdh_integration.sh
```

### Spark Output

```sh
root
 |-- key: string (nullable = true)
 |-- name: string (nullable = true)
 |-- age: string (nullable = true)
 |-- designation: string (nullable = true)
 |-- salary: string (nullable = true)

+---+--------+---+------------------------+------+
|key|name    |age|designation             |salary|
+---+--------+---+------------------------+------+
|1  |Ranga   |32 |Software Engineer       |60000 |
|2  |Nishanth|3  |Junior Software Engineer|80000 |
+---+--------+---+------------------------+------+
```