# Spark Phoenix Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/dbs/nosql/phoenix/phoenix_logo.png?raw=true" height="200" width="250"/>
</div>


## Prerequisites

* Spark Version : 2.4.0.7.1.6.0-297
* Phoenix Version : 6.0.0.7.1.6.0-297
* Java Version : 1.8
* Scala Version : 2.11.12

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


## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-phoenix-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-phoenix-integration
$ chmod 755 /apps/spark/spark-phoenix-integration
```

## Download the `spark-phoenix-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-phoenix-integration
```

## Build the `spark-phoenix-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-phoenix-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_phoenix_integration_app.sh` to spark gateway node `/apps/spark/spark-phoenix-integration` directory.
```sh
$ scp target/spark-phoenix-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-phoenix-integration
$ scp run_spark_phoenix_integration_app.sh username@mynode.host.com:/apps/spark/spark-phoenix-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_phoenix_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_phoenix_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-phoenix-integration/run_spark_phoenix_integration_app.sh
```