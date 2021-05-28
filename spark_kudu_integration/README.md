# Spark Hive Kudu Integration

## Creating a Kudu table from Impala

### Login to impala shell

```shell
impala-shell
```
### Creating the Kudu table
```sql
CREATE TABLE IF NOT EXISTS kudu_emp_table
(
	id INT,
	name STRING,
	age SMALLINT, 
	designation STRING, 
	salary BIGINT,
	PRIMARY KEY(id)
) PARTITION BY HASH PARTITIONS 16
STORED AS KUDU;
```
### INSERT the data to Kudu table
```sql
INSERT INTO kudu_emp_table VALUES (1, 'Ranga Reddy', 32, 'Senior Software Engineer', 50000);
INSERT INTO kudu_emp_table VALUES (2, 'Nishanth Reddy', 3, 'Software Engineer', 80000);
```
### Selecting the kudu table data
```
select * from kudu_emp_table;
+----+----------------+-----+--------------------------+--------+
| id | name           | age | designation              | salary |
+----+----------------+-----+--------------------------+--------+
| 1  | Ranga Reddy    | 32  | Senior Software Engineer | 50000  |
| 2  | Nishanth Reddy | 3   | Software Engineer        | 80000  |
+----+----------------+-----+--------------------------+--------+
Fetched 2 row(s) in 0.33s
```
Quit the impala shell.

### List the kudu tables using CLI
```
kudu table list host1:7051,host2:7051,host3:7051
impala::default.kudu_emp_table
```
## Creating a Hive table using Kudu as a external table

### Creating the hive table

```sql
CREATE EXTERNAL TABLE IF NOT EXISTS hive_kudu_emp_table 
(
	id INT,
	name STRING,
	age SMALLINT, 
	designation STRING, 
	salary BIGINT
)
STORED BY 'org.apache.hadoop.hive.kudu.KuduStorageHandler'
TBLPROPERTIES (
  "kudu.table_name"="impala::default.kudu_emp_table", 
  "kudu.master_addresses"="host1:7051,host2:7051,host3:7051"
);
```
Note: If kudu table is created using Impala then table name Syntax will be **impala::database_name.table_name**

### Selecting the hive table data
```
SELECT * FROM hive_kudu_emp_table;
+-------------------------+---------------------------+--------------------------+----------------------------------+-----------------------------+
| hive_kudu_emp_table.id  | hive_kudu_emp_table.name  | hive_kudu_emp_table.age  | hive_kudu_emp_table.designation  | hive_kudu_emp_table.salary  |
+-------------------------+---------------------------+--------------------------+----------------------------------+-----------------------------+
| 1                  	  | Ranga Reddy               | 32                       | Senior Software Engineer         | 50000                       |
| 2                       | Nishanth Reddy            | 3                        | Software Engineer                | 80000                       |
+-------------------------+---------------------------+--------------------------+----------------------------------+-----------------------------+
2 rows selected (17.401 seconds)
```

## Accessing the Kudu table directly

### Launch the Spark-shell
```shell
sudo -u spark spark-shell --packages org.apache.kudu:kudu-spark2_2.11:1.9.0-cdh6.2.0 --repositories https://repository.cloudera.com/artifactory/cloudera-repos/
```
### Loading the kudu table data with out using KudoContext

```scala
val kuduMasters: String = "host1:7051,host2:7051,host3:7051"
//val tableName: String = "default.kudu_emp_table"
val tableName: String = "impala::default.kudu_emp_table" // kudu table is created using impala so we need to add prefix impala::

val kuduPropMap = Map("kudu.master" -> kuduMasters, "kudu.table" -> tableName)
val employeeDF = spark.read.options(kuduPropMap).format("kudu").load

scala> employeeDF.printSchema()
root
 |-- id: integer (nullable = false)
 |-- name: string (nullable = true)
 |-- age: short (nullable = true)
 |-- designation: string (nullable = true)
 |-- salary: long (nullable = true)

scala> employeeDF.show(truncate=false)
+---+--------------+---+------------------------+------+
|id |name          |age|designation             |salary|
+---+--------------+---+------------------------+------+
|1  |Ranga Reddy   |32 |Senior Software Engineer|50000 |
|2  |Nishanth Reddy|3  |Software Engineer       |80000 |
+---+--------------+---+------------------------+------+
```

## Accessing the Kudu table data using Hive table

### Find the **hive-kudu** handler jar
```
ls /opt/cloudera/parcels/CDH/jars | grep 'hive-kudu'
hive-kudu-handler-3.1.3000.7.1.1.0-565.jar
```

### Launch the Spark-shell
```shell
sudo -u spark spark-shell --jars /opt/cloudera/parcels/CDH/jars/hive-kudu-handler-3.1.3000.7.1.1.0-565.jar
```

### Loading the hive table data
```
val employeeDF = spark.sql("select * from hive_kudu_emp_table")
scala> employeeDF.printSchema()
root
 |-- id: integer (nullable = true)
 |-- name: string (nullable = true)
 |-- age: short (nullable = true)
 |-- designation: string (nullable = true)
 |-- salary: long (nullable = true)

scala> employeeDF.show(truncate=false)
+---+--------------+---+------------------------+------+
|id |name          |age|designation             |salary|
+---+--------------+---+------------------------+------+
|1  |Ranga Reddy   |32 |Senior Software Engineer|50000 |
|2  |Nishanth Reddy|3  |Software Engineer       |80000 |
+---+--------------+---+------------------------+------+
```

## Kudu CRUD Operation Using KuduContext.
```scala

case class Employee(id:Int, name:String, age:Short, designation:String, salary:Long)
val empData = List(Employee(1, "Ranga Reddy", 32, "Senior Software Engineer", 50000), Employee(2, "Nishanth Reddy", 3, "Software Engineer", 80000), Employee(3, "Manoj", 15, "HR", 49000), Employee(4, "Yasu", 10, "Manager", 190000))
val empDF = spark.createDataset(empData).toDF

val kuduMasters: String = "host1:7051,host2:7051,host3:7051"
val tableName: String = "default.kudu_employee_table"

import org.apache.kudu.client._
import org.apache.kudu.spark.kudu._
import collection.JavaConverters._

val kuduContext = new KuduContext(kuduMasters, spark.sqlContext.sparkContext)

// Create a Kudu table
kuduContext.createTable(tableName, empDF.schema, Seq("id"), new CreateTableOptions().setNumReplicas(1).addHashPartitions(List("id").asJava, 3))

// Insert data
kuduContext.insertRows(empDF, tableName)

// Read a table from Kudu
val kuduPropMap = Map("kudu.master" -> kuduMasters, "kudu.table" -> tableName)
val employeeDF = spark.read.options(kuduPropMap).format("kudu").load

//val deleteDF = spark.createDataFrame(Seq((4, "Yasu"))).toDF("id", "name")

// Delete data
//kuduContext.deleteRows(empDF, tableName)

// Select Data
//val employeeDF = spark.read.options(kuduPropMap).format("kudu").load

// Upserts
//val upsertEmployees = List(Employee(4, "Raja Sekahar Reddy", 10, "Manager", 190000), Employee(4, "Yasu", 10, "Manager", 190000))
//val upsertEmployeeDF = spark.createDataset(empData).toDF
//kuduContext.upsertRows(upsertEmployeeDF, tableName)

// Delete Kudu table
kuduContext.deleteTable(tableName)

// Checking table exists or not
kuduContext.tableExists(tableName)
```

** References**
1. https://kudu.apache.org/docs/hive_metastore.html
2. https://kudu.apache.org/docs/developing.html
3. https://cwiki.apache.org/confluence/display/Hive/Kudu+Integration
4. https://github.com/apache/kudu/blob/master/examples/scala/spark-example/src/main/scala/org/apache/kudu/spark/examples/SparkExample.scala
