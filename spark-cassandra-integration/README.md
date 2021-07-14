# Spark Cassandra Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/dbs/nosql/cassandra/cassandra_logo.png?raw=true" height="200" width="250"/>
</div>


## Prerequisites

* Spark Version : 2.4.0.7.1.6.0-297
* Spark Cassandra Connector Version : 3.0.0
* Java Version : 1.8
* Scala Version : 2.11.12

## Cassandra schema

### Launch `cqlsh` shell
```sh
cqlsh
```

### Create a keyspace
```sql
cqlsh> CREATE KEYSPACE IF NOT EXISTS ranga_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 };
cqlsh> use ranga_keyspace;
```

### Create a table
```sql
cqlsh:ranga_keyspace> CREATE TABLE IF NOT EXISTS ranga_keyspace.employees(id bigint PRIMARY KEY, name TEXT, age int, salary float);
```

### Insert the data
```sql
cqlsh:ranga_keyspace> INSERT INTO ranga_keyspace.employees(id, name, age, salary) VALUES (1, 'Ranga Reddy', 33, 50000.00);
cqlsh:ranga_keyspace> INSERT INTO ranga_keyspace.employees(id, name, age, salary) VALUES (2, 'Nishanth Reddy', 4, 80000.00);
cqlsh:ranga_keyspace> INSERT INTO ranga_keyspace.employees(id, name, age, salary) VALUES (3, 'Raja Sekhar Reddy', 60, 150000.00);
cqlsh:ranga_keyspace> INSERT INTO ranga_keyspace.employees(id, name, age, salary) VALUES (4, 'Mani Reddy', 16, 90000.00);
```

### Select the data
```sql
cqlsh:ranga_keyspace> SELECT * FROM ranga_keyspace.employees;

 id | age | name              | salary
----+-----+-------------------+---------
  2 |   4 |    Nishanth Reddy |   80000
  3 |  60 | Raja Sekhar Reddy | 1.5e+05
  4 |  16 |        Mani Reddy |   90000
  1 |  33 |       Ranga Reddy |   50000

(4 rows)
```

## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-cassandra-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-cassandra-integration
$ chmod 755 /apps/spark/spark-cassandra-integration
```

## Download the `spark-cassandra-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-cassandra-integration
```

## Build the `spark-cassandra-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-cassandra-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_cassandra_integration_app.sh` to spark gateway node `/apps/spark/spark-cassandra-integration` directory.
```sh
$ scp target/spark-cassandra-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-cassandra-integration
$ scp run_spark_cassandra_integration_app.sh username@mynode.host.com:/apps/spark/spark-cassandra-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_cassandra_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_cassandra_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-cassandra-integration/run_spark_cassandra_integration_app.sh
```