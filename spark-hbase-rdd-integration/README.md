# Spark Hbase RDD Integration

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

## Run the code as Project

### Create the deployment directory in edge node.

Login to spark gateway node (for example mynode.host.com) and create the application deployment `/apps/spark/spark-hbase-rdd-integration` directory.

```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-hbase-rdd-integration
$ chmod 755 /apps/spark/spark-hbase-rdd-integration
```

### Download the `spark-hbase-rdd-integration` application.

```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-hbase-rdd-integration
```

### Build and deploy the `spark-hbase-rdd-integration` application.

#### Building the project using `maven` build tool.

```sh
$ mvn clean package
```

#### Copy the `spark-hbase-rdd-integration-1.0.0-SNAPSHOT.jar` uber jar to spark gateway node `/apps/spark/spark-hbase-rdd-integration` directory.

```sh
$ scp target/spark-hbase-rdd-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-hbase-rdd-integration
```

#### Copy the run script `run_spark_hbase_rdd_integration_app.sh` to spark gateway node `/apps/spark/spark-hbase-rdd-integration` directory.

```sh
$ scp run_spark_hbase_rdd_integration_app.sh username@mynode.host.com:/apps/spark/spark-hbase-rdd-integration
```

### Run the application

#### Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_hbase_rdd_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_hbase_rdd_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-hbase-rdd-integration/run_spark_hbase_rdd_integration_app.sh
```