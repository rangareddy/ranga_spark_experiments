# Spark Cassandra Integration

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

