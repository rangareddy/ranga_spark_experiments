# Spark Cassandra Integration

```sh
Create a keyspace called “test_spark” in Cassandra
create the table test_spark.test (value int PRIMARY KEY); in the test_spark keycap
Insert some data (INSERT INTO test_spark (value) VALUES (1);)
```

## Cassandra schema

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

## Loading up the Spark-Shell
```sh
spark-shell --conf spark.cassandra.connection.host=127.0.0.1 \
                            --packages com.datastax.spark:spark-cassandra-connector_2.11:2.4.1
```

### Enable Cassandra-specific functions
```sh
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
```

### Saving data from RDD to Cassandra
```sh

case class Employee(id:Long, name: String, age: Integer, salary: Float)
import spark.implicits._
var employeeDS = Seq(
    Employee(1L, "Ranga Reddy", 32, 80000.5f),
    Employee(2L, "Nishanth Reddy", 3, 180000.5f),
    Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f)
).toDS()

val collection = sc.parallelize(Seq(("key3", 3), ("key4", 4)))
collection.saveToCassandra("test", "kv", SomeColumns("key", "value")) 

df.write
  .format("org.apache.spark.sql.cassandra")
  .options(Map("table" -> "words_copy", "keyspace" -> "test", "cluster" -> "cluster_B"))
  .save()

```

### Loading and analyzing data from Cassandra
```sh
val employeeDF = spark.read.format("org.apache.spark.sql.cassandra").options(Map( "keyspace" -> "ranga_keyspace", "table" -> "employees")).load()
employeeDF.show(truncate=false)
```
