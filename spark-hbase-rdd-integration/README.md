# Spark Hbase Integration

<div>
    <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
    <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
    <img src="https://github.com/rangareddy/ranga-logos/blob/main/dbs/nosql/hbase/hbase_logo.png?raw=true" height="200" width="250"/>
</div>

## Prerequisites

| Component   | Version              |
|-------------|----------------------|
| Scala       | 2.11.12              |
| Java        | 1.8                  |
| Spark       | 2.4.7.7.1.7.1000-141 |
| Hbase Spark | 2.2.3.7.1.7.1000-141 |

## Launch HBase Shell and create an Employee table

### Launch the hbase shell using following command.

```sh
hbase shell
```

### Create an Employee table

```sql
create 'employees', 'e'
```

```sql
put 'employees', '1', 'e:id', '1'
put 'employees', '1', 'e:name', 'Ranga'
put 'employees', '1', 'e:age', '34'
put 'employees', '1', 'e:salary', '10000'
put 'employees', '2', 'e:id', '2'
put 'employees', '2', 'e:name', 'Nishanth'
put 'employees', '2', 'e:age', '5'
put 'employees', '2', 'e:salary', '50000'
```