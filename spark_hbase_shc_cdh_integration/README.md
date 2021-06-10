# CDH Spark HBase Integration using SHC

## Create and Insert the data to `employee` table in HBase

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
### Verify the HBase table data
```
scan 'employee'
ROW                                                              COLUMN+CELL
 1                                                               column=per:age, timestamp=1623301157496, value=32
 1                                                               column=per:name, timestamp=1623301157444, value=Ranga
 1                                                               column=prof:designation, timestamp=1623301157546, value=Software Engineer
 1                                                               column=prof:salary, timestamp=1623301157612, value=60000
 2                                                               column=per:age, timestamp=1623301157705, value=3
 2                                                               column=per:name, timestamp=1623301157671, value=Nishanth
 2                                                               column=prof:designation, timestamp=1623301157727, value=Junior Software Engineer
 2                                                               column=prof:salary, timestamp=1623301161097, value=80000
2 row(s)
Took 0.0991 seconds
```

## Create the application deployment directory in spark gateway node. for example `/apps/spark/spark-hbase/`.
```sh
$ ssh username@node2.host.com
$ mkdir -p /apps/spark/spark-hbase/
$ chmod 755 /apps/spark/spark-hbase/
```

## Building the `shc-core` project.

```sh
$ git clone https://github.com/hortonworks-spark/shc.git
$ cd shc/
$ mvn clean install -DskipTests
```

## Download the `spark_hbase_shc_cdh_integration` project.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark_hbase_shc_cdh_integration
```

### Build the `spark_hbase_shc_cdh_integration` application.

> Before building the project update your spark version according to your cluster version.

```sh
$ mvn clean package -DskipTests
```

### Copy the `spark_hbase_shc_integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_hbase_shc_cdh_integration.sh` to spark gateway node `/apps/spark/spark-hbase/` directory.
```sh
$ scp target/spark_hbase_shc_integration-1.0.0-SNAPSHOT.jar root@node2.host.com:/apps/spark/spark-hbase/
$ scp run_spark_hbase_shc_cdh_integration.sh root@node2.host.com:/apps/spark/spark-hbase/
```

### Login to gateway node and run the `run_spark_hbase_shc_cdh_integration.sh` shell script.
```sh
sh /apps/spark/spark-hbase/run_spark_hbase_shc_cdh_integration.sh
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
