# Spark HBase Integration

## HDP - Spark HBase Integration

### Launch HBase Shell and create an Employee table
```
# hbase shell
# create 'employees', 'e'
```

### Testing the Application using spark-shell

#### Launch the spark-shell
```sh
spark-shell --master yarn --executor-cores 5 --deploy-mode client \
--jars /usr/hdp/current/hbase-client/lib/hbase-client.jar,\
/usr/hdp/current/hbase-client/lib/hbase-common.jar,\
/usr/hdp/current/hbase-client/lib/hbase-server.jar,\
/usr/hdp/current/hbase-client/lib/hbase-mapreduce.jar,\
/usr/hdp/current/hbase-client/lib/hbase-protocol.jar,\
/usr/hdp/current/hbase-client/lib/hbase-protocol-shaded.jar,\
/usr/hdp/current/hbase-client/lib/hbase-spark.jar,\
/usr/hdp/current/hbase-client/lib/hbase-zookeeper.jar,\
/usr/hdp/current/hbase-client/lib/hbase-hadoop2-compat.jar,\
/usr/hdp/current/hbase-client/lib/hbase-shaded-netty-2.2.0.jar,\
/usr/hdp/current/hbase-client/lib/hbase-shaded-protobuf-2.2.0.jar,\
/usr/hdp/current/hbase-client/lib/hbase-shaded-miscellaneous-2.2.0.jar,\
/usr/hdp/current/hbase-client/lib/hbase-replication.jar,\
/usr/hdp/current/hbase-client/lib/hbase-procedure.jar,\
/usr/hdp/current/hbase-client/lib/hbase-metrics.jar,\
/usr/hdp/current/hbase-client/lib/hbase-http.jar,\
/usr/hdp/current/hbase-client/lib/hbase-hadoop-compat.jar,\
/usr/hdp/current/hbase-client/lib/phoenix-server.jar \
--files /etc/hbase/conf/hbase-site.xml
```

#### Execute the following spark code in spark-shell
```scala
import org.apache.hadoop.hbase.spark.HBaseContext
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.fs.Path

val conf = HBaseConfiguration.create()
conf.addResource(new Path("/etc/hbase/conf/hbase-site.xml"))
new HBaseContext(spark.sparkContext, conf)

case class Employee(id:Long, name: String, age: Integer, salary: Float)

import spark.implicits._
var employeeDS = Seq(
Employee(1L, "Ranga Reddy", 32, 80000.5f),
Employee(2L, "Nishanth Reddy", 3, 180000.5f),
Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f)
).toDS()

val columnMapping = "id Long :key, name STRING e:name, age Integer e:age, salary FLOAT e:salary"
val format = "org.apache.hadoop.hbase.spark"
val tableName = "employees"

// write the data to hbase table
employeeDS.write.format(format).option("hbase.columns.mapping",columnMapping).option("hbase.table", tableName).save()

// read the data from hbase table
val df = spark.read.format(format).option("hbase.columns.mapping",columnMapping).option("hbase.table", tableName).load()
df.show(truncate=false)
```

### Testing the Application using spark-submit

#### Building the application
```sh
mvn clean package -DskipTests
```

#### Login to edge node and create the following directory
```sh
mkdir -p /usr/apps/spark/spark-hbase/
```

#### copy the jar and run script to edge node
```sh
scp target/spark-hase-integration-1.0.0-SNAPSHOT.jar username@hostname:/usr/apps/spark/spark-hbase/
scp run_spark_hbase_integration.sh username@hostname:/usr/apps/spark/spark-hbase/
```

#### Run the script
```sh
sh run_spark_hbase_integration.sh
```

### HBase output in spark-shell
```sh
+---+-----------------+--------+---+
|age|name             |salary  |id |
+---+-----------------+--------+---+
|32 |Ranga Reddy      |80000.5 |1  |
|3  |Nishanth Reddy   |180000.5|2  |
|59 |Raja Sekhar Reddy|280000.5|3  |
+---+-----------------+--------+---+
```