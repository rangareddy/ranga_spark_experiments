# Phoenix Spark connector

The phoenix-spark plugin extends Phoenix’s MapReduce support to allow Spark to load Phoenix tables as DataFrames, and enables persisting them back to Phoenix.

**Limitations:**
* Basic support for column and predicate pushdown using the Data Source API
* The Data Source API does not support passing custom Phoenix settings in configuration, you must create the DataFrame or RDD directly if you need fine-grained configuration.
* No support for aggregate or distinct queries as explained in our Map Reduce Integration documentation.

The following example demonstrate the **Spark Phoenix** integration

* [CDP Integration](#cdp-integration)
* [HDP Integration](#hdp-integration)
* [HDP Kerberized Integration](#hdp-kerberized-integration)

## CDP Integration

### Step1: Launch the Phoenix Shell using sqlline.py
```sh
python /opt/cloudera/parcels/CDH/lib/phoenix/bin/sqlline.py
```

### Step2: Create an EMPLOYEE table in Phoenix
```sql
> CREATE TABLE IF NOT EXISTS EMPLOYEE (
  ID BIGINT NOT NULL, 
  NAME VARCHAR, 
  AGE INTEGER, 
  SALARY FLOAT
  CONSTRAINT emp_pk PRIMARY KEY (ID)
);
```

### Step3: List the Phoenix tables
```sql
> !tables
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
| TABLE_CAT  | TABLE_SCHEM  | TABLE_NAME  |  TABLE_TYPE   | REMARKS  | TYPE_NAME  | SELF_REFERENCING_COL_NAME  | REF_GENERATION  | INDEX_STATE  | IMMUTABLE_ROWS  | SALT_BUCKETS  | MUL |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
|            | SYSTEM       | CATALOG     | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | FUNCTION    | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | LOG         | SYSTEM TABLE  |          |            |                            |                 |              | true            | 32            | fal |
|            | SYSTEM       | SEQUENCE    | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | STATS       | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            |              | EMPLOYEE    | TABLE         |          |            |                            |                 |              | false           | null          | fal |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
```

### Step4: Insert the data to EMPLOYEE table in Phoenix
```sql
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (1, 'Ranga', 32, 10000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (2, 'Nishanth', 2, 30000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (3, 'Raja', 52, 60000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (4, 'Yashu', 10, 8000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (5, 'Manoj', 16, 15000);
```

### Step5: Select the Employee data
```sql
> SELECT * FROM EMPLOYEE;
+-----+-----------+------+----------+
| ID  |   NAME    | AGE  |  SALARY  |
+-----+-----------+------+----------+
| 1   | Ranga     | 32   | 10000.0  |
| 2   | Nishanth  | 2    | 30000.0  |
| 3   | Raja      | 52   | 60000.0  |
| 4   | Yashu     | 10   | 8000.0   |
| 5   | Manoj     | 16   | 15000.0  |
+-----+-----------+------+----------+
5 rows selected (0.07 seconds)

> !quit
```

### Step6: Login to hbase shell and check the EMPLOYEE table data in HBase
```sh
hbase shell

> scan 'EMPLOYEE'
ROW                                             COLUMN+CELL
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x00\x00\x00\x00, timestamp=1615911856369, value=x
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0B, timestamp=1615911856369, value=Ranga
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0C, timestamp=1615911856369, value=\x80\x00\x00
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0D, timestamp=1615911856369, value=\xC6\x1C@\x01
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x00\x00\x00\x00, timestamp=1615911856418, value=x
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0B, timestamp=1615911856418, value=Nishanth
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0C, timestamp=1615911856418, value=\x80\x00\x00\x02
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0D, timestamp=1615911856418, value=\xC6\xEA`\x01
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x00\x00\x00\x00, timestamp=1615911856448, value=x
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0B, timestamp=1615911856448, value=Raja
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0C, timestamp=1615911856448, value=\x80\x00\x004
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0D, timestamp=1615911856448, value=\xC7j`\x01
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x00\x00\x00\x00, timestamp=1615911856478, value=x
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0B, timestamp=1615911856478, value=Yashu
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0C, timestamp=1615911856478, value=\x80\x00\x00\x0A
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0D, timestamp=1615911856478, value=\xC5\xFA\x00\x01
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x00\x00\x00\x00, timestamp=1615911856507, value=x
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0B, timestamp=1615911856507, value=Manoj
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0C, timestamp=1615911856507, value=\x80\x00\x00\x10
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0D, timestamp=1615911856507, value=\xC6j`\x01
 
 > exit
```

### Step7: Launch Spark-shell by providing the phoenix-spark and phoenix-client jar
```sh  
sudo -u spark spark-shell \
  --master yarn \
  --jars /opt/cloudera/parcels/CDH/jars/phoenix5-spark-*.jar,/opt/cloudera/parcels/CDH/jars/phoenix-client-hbase-*.jar \
  --files /etc/hbase/conf/hbase-site.xml
```

### Step8: Run the Spark code in spark-shell
```scala
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.SparkConf

val conf = new SparkConf().setAppName("Phoenix Spark connector example").setIfMissing("spark.master", "local[*]")
val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
val sqlContext = spark.sqlContext

val zkUrl="phoenix-server:2181"
//val zkUrl="phoenix-server:2181/hbase-secure"

val employeeDf = sqlContext.load(
  "org.apache.phoenix.spark",
  Map("table" -> "EMPLOYEE", "zkUrl" -> zkUrl)
)

employeeDf.show(4)
```

**Output**
```
+---+--------+---+-------+
| ID|    NAME|AGE| SALARY|
+---+--------+---+-------+
|  1|   Ranga| 32|10000.0|
|  2|Nishanth|  2|30000.0|
|  3|    Raja| 52|60000.0|
|  4|   Yashu| 10| 8000.0|
+---+--------+---+-------+
```
_________________

## HDP Integration

### Step1: Launch the Phoenix Shell using sqlline.py
```sh
python /usr/hdp/current/phoenix-client/bin/sqlline.py
```

### Step2: Create an EMPLOYEE table in Phoenix
```sql
> CREATE TABLE IF NOT EXISTS EMPLOYEE (
  ID BIGINT NOT NULL, 
  NAME VARCHAR, 
  AGE INTEGER, 
  SALARY FLOAT
  CONSTRAINT emp_pk PRIMARY KEY (ID)
);
```

### Step3: List the Phoenix tables
```sql
> !tables
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
| TABLE_CAT  | TABLE_SCHEM  | TABLE_NAME  |  TABLE_TYPE   | REMARKS  | TYPE_NAME  | SELF_REFERENCING_COL_NAME  | REF_GENERATION  | INDEX_STATE  | IMMUTABLE_ROWS  | SALT_BUCKETS  | MUL |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
|            | SYSTEM       | CATALOG     | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | FUNCTION    | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | LOG         | SYSTEM TABLE  |          |            |                            |                 |              | true            | 32            | fal |
|            | SYSTEM       | SEQUENCE    | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | STATS       | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            |              | EMPLOYEE    | TABLE         |          |            |                            |                 |              | false           | null          | fal |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
```

### Step4: Insert the data to Employee table
```sql
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (1, 'Ranga', 32, 10000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (2, 'Nishanth', 2, 30000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (3, 'Raja', 52, 60000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (4, 'Yashu', 10, 8000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (5, 'Manoj', 16, 15000);
```

### Step5: Select the Employee data
```sql
> SELECT * FROM EMPLOYEE;
+-----+-----------+------+----------+
| ID  |   NAME    | AGE  |  SALARY  |
+-----+-----------+------+----------+
| 1   | Ranga     | 32   | 10000.0  |
| 2   | Nishanth  | 2    | 30000.0  |
| 3   | Raja      | 52   | 60000.0  |
| 4   | Yashu     | 10   | 8000.0   |
| 5   | Manoj     | 16   | 15000.0  |
+-----+-----------+------+----------+
5 rows selected (0.07 seconds)

> !quit
```

### Step6: Login to hbase shell and check the EMPLOYEE table data in HBase
```sh
hbase shell

> scan 'EMPLOYEE'
ROW                                             COLUMN+CELL
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x00\x00\x00\x00, timestamp=1615911856369, value=x
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0B, timestamp=1615911856369, value=Ranga
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0C, timestamp=1615911856369, value=\x80\x00\x00
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0D, timestamp=1615911856369, value=\xC6\x1C@\x01
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x00\x00\x00\x00, timestamp=1615911856418, value=x
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0B, timestamp=1615911856418, value=Nishanth
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0C, timestamp=1615911856418, value=\x80\x00\x00\x02
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0D, timestamp=1615911856418, value=\xC6\xEA`\x01
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x00\x00\x00\x00, timestamp=1615911856448, value=x
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0B, timestamp=1615911856448, value=Raja
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0C, timestamp=1615911856448, value=\x80\x00\x004
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0D, timestamp=1615911856448, value=\xC7j`\x01
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x00\x00\x00\x00, timestamp=1615911856478, value=x
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0B, timestamp=1615911856478, value=Yashu
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0C, timestamp=1615911856478, value=\x80\x00\x00\x0A
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0D, timestamp=1615911856478, value=\xC5\xFA\x00\x01
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x00\x00\x00\x00, timestamp=1615911856507, value=x
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0B, timestamp=1615911856507, value=Manoj
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0C, timestamp=1615911856507, value=\x80\x00\x00\x10
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0D, timestamp=1615911856507, value=\xC6j`\x01
 
 > exit
```

### Step7: Launch Spark-shell by providing the phoenix-spark and phoenix-client jar
```sh
spark-shell \
  --master yarn \
  --jars /usr/hdp/current/phoenix-client/lib/phoenix-spark*.jar,/usr/hdp/current/phoenix-client/phoenix-client.jar \
  --files /etc/hbase/conf/hbase-site.xml
```

### Step8: Run the Spark code in spark-shell
```scala
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.SparkConf

val conf = new SparkConf().setAppName("Phoenix Spark connector example").setIfMissing("spark.master", "local[*]")
val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
val sqlContext = spark.sqlContext

val zkUrl="localhost:2181"
//val zkUrl="localhost:2181/hbase-unsecure"

val employeeDf = sqlContext.load(
  "org.apache.phoenix.spark",
  Map("table" -> "EMPLOYEE", "zkUrl" -> zkUrl)
)

employeeDf.show(4)
```
**Output**
```sql
+---+--------+---+-------+
| ID|    NAME|AGE| SALARY|
+---+--------+---+-------+
|  1|   Ranga| 32|10000.0|
|  2|Nishanth|  2|30000.0|
|  3|    Raja| 52|60000.0|
|  4|   Yashu| 10| 8000.0|
+---+--------+---+-------+
```
_________________

## HDP Kerberized Integration

### Step1: Kinit with hbase user
```sh
kinit -kt /etc/security/keytabs/hbase.headless.keytab hbase-host1@HADOOP.COM
```
### Step2: Launch the Phoenix Shell using sqlline.py
```sh
python /usr/hdp/current/phoenix-client/bin/sqlline.py
```

### Step3: Create an EMPLOYEE table in Phoenix
```sql
> CREATE TABLE IF NOT EXISTS EMPLOYEE (
  ID BIGINT NOT NULL, 
  NAME VARCHAR, 
  AGE INTEGER, 
  SALARY FLOAT
  CONSTRAINT emp_pk PRIMARY KEY (ID)
);
```

### Step4: List the Phoenix tables
```sql
> !tables
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
| TABLE_CAT  | TABLE_SCHEM  | TABLE_NAME  |  TABLE_TYPE   | REMARKS  | TYPE_NAME  | SELF_REFERENCING_COL_NAME  | REF_GENERATION  | INDEX_STATE  | IMMUTABLE_ROWS  | SALT_BUCKETS  | MUL |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
|            | SYSTEM       | CATALOG     | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | FUNCTION    | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | LOG         | SYSTEM TABLE  |          |            |                            |                 |              | true            | 32            | fal |
|            | SYSTEM       | SEQUENCE    | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            | SYSTEM       | STATS       | SYSTEM TABLE  |          |            |                            |                 |              | false           | null          | fal |
|            |              | EMPLOYEE    | TABLE         |          |            |                            |                 |              | false           | null          | fal |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----------------+---------------+-----+
```

### Step5: Insert the data to EMPLOYEE table in Phoenix
```sql
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (1, 'Ranga', 32, 10000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (2, 'Nishanth', 2, 30000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (3, 'Raja', 52, 60000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (4, 'Yashu', 10, 8000);
UPSERT INTO EMPLOYEE (ID, NAME, AGE, SALARY) VALUES (5, 'Manoj', 16, 15000);
```

### Step6: Select the Employee data
```sql
> SELECT * FROM EMPLOYEE;
+-----+-----------+------+----------+
| ID  |   NAME    | AGE  |  SALARY  |
+-----+-----------+------+----------+
| 1   | Ranga     | 32   | 10000.0  |
| 2   | Nishanth  | 2    | 30000.0  |
| 3   | Raja      | 52   | 60000.0  |
| 4   | Yashu     | 10   | 8000.0   |
| 5   | Manoj     | 16   | 15000.0  |
+-----+-----------+------+----------+
5 rows selected (0.07 seconds)

> !quit
```

### Step7: Login to hbase shell and check the EMPLOYEE table data in HBase
```sh
hbase shell

> scan 'EMPLOYEE'
ROW                                             COLUMN+CELL
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x00\x00\x00\x00, timestamp=1615911856369, value=x
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0B, timestamp=1615911856369, value=Ranga
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0C, timestamp=1615911856369, value=\x80\x00\x00
 \x80\x00\x00\x00\x00\x00\x00\x01               column=0:\x80\x0D, timestamp=1615911856369, value=\xC6\x1C@\x01
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x00\x00\x00\x00, timestamp=1615911856418, value=x
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0B, timestamp=1615911856418, value=Nishanth
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0C, timestamp=1615911856418, value=\x80\x00\x00\x02
 \x80\x00\x00\x00\x00\x00\x00\x02               column=0:\x80\x0D, timestamp=1615911856418, value=\xC6\xEA`\x01
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x00\x00\x00\x00, timestamp=1615911856448, value=x
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0B, timestamp=1615911856448, value=Raja
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0C, timestamp=1615911856448, value=\x80\x00\x004
 \x80\x00\x00\x00\x00\x00\x00\x03               column=0:\x80\x0D, timestamp=1615911856448, value=\xC7j`\x01
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x00\x00\x00\x00, timestamp=1615911856478, value=x
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0B, timestamp=1615911856478, value=Yashu
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0C, timestamp=1615911856478, value=\x80\x00\x00\x0A
 \x80\x00\x00\x00\x00\x00\x00\x04               column=0:\x80\x0D, timestamp=1615911856478, value=\xC5\xFA\x00\x01
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x00\x00\x00\x00, timestamp=1615911856507, value=x
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0B, timestamp=1615911856507, value=Manoj
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0C, timestamp=1615911856507, value=\x80\x00\x00\x10
 \x80\x00\x00\x00\x00\x00\x00\x05               column=0:\x80\x0D, timestamp=1615911856507, value=\xC6j`\x01
 
 > exit
```

### Step8: Launch Spark-shell by providing the phoenix-spark and phoenix-client jar
```sh
spark-shell \
  --master yarn \
  --jars /usr/hdp/current/phoenix-client/lib/phoenix-spark*.jar,/usr/hdp/current/phoenix-client/phoenix-client.jar \
  --files /etc/hbase/conf/hbase-site.xml
```

### Step9: Run the Spark code in spark-shell
```scala
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.SparkConf

val conf = new SparkConf().setAppName("Phoenix Spark connector example").setIfMissing("spark.master", "local[*]")
val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
val sqlContext = spark.sqlContext

val zkUrl="phoenix-server:2181"
//val zkUrl="phoenix-server:2181/hbase-secure"

// Load data from EMPLOYEE
val employeeDf = sqlContext.load(
  "org.apache.phoenix.spark",
  Map("table" -> "EMPLOYEE", "zkUrl" -> zkUrl)
)

employeeDf.show(4)
```
**Output**
```
+---+--------+---+-------+
| ID|    NAME|AGE| SALARY|
+---+--------+---+-------+
|  1|   Ranga| 32|10000.0|
|  2|Nishanth|  2|30000.0|
|  3|    Raja| 52|60000.0|
|  4|   Yashu| 10| 8000.0|
+---+--------+---+-------+
```
_________________

**References:**
* https://blogs.apache.org/phoenix/entry/spark_integration_in_apache_phoenix
* https://phoenix.apache.org/phoenix_spark.html
* https://docs.cloudera.com/runtime/7.2.9/phoenix-access-data/topics/phoenix-understanding-spark-connector.html
* https://docs.cloudera.com/documentation/enterprise/6/6.3/topics/phoenix_spark_connector.html
