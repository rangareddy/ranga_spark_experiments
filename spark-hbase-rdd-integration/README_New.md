# Spark Hbase Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/dbs/nosql/hbase/hbase_logo.png?raw=true" height="200" width="250"/>
</div>


# Table of Contents
-------------------------
## 1.  Prerequisites
## 2.  Launch HBase Shell and create an Employee table
## 3.  Run the code as Project

## Prerequisites

|Component|Version|
|---------|-------|
|Scala|2.11.12|
|Java|1.8|
|Spark|2.4.0.7.1.6.0-297|
|Hbase Spark|1.0.0.7.1.6.0-297|


## Launch HBase Shell and create an Employee table

### Launch the hbase shell using following command.
```sh
hbase shell
```

### Create the an Employee table
```sql
create 'employees', 'e'
```

## Run the code as Project

### Create the deployment directory in edge node.

Login to spark gateway node (for example mynode.host.com) and create the application deployment `/apps/spark/spark-hbase-integration` directory.

```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-hbase-integration
$ chmod 755 /apps/spark/spark-hbase-integration
```

### Download the `spark-hbase-integration` application.

```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-hbase-integration
```

### Build and deploy the `spark-hbase-integration` application.

#### Building the project using `maven` build tool.

```sh
$ mvn clean package
```

#### Copy the `spark-hbase-integration-1.0.0-SNAPSHOT.jar` uber jar to spark gateway node `/apps/spark/spark-hbase-integration` directory.

```sh
$ scp target/spark-hbase-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-hbase-integration
```


#### Copy the run script `run_spark_hbase_integration_app.sh` to spark gateway node `/apps/spark/spark-hbase-integration` directory.

```sh
$ scp run_spark_hbase_integration_app.sh username@mynode.host.com:/apps/spark/spark-hbase-integration
```

### Run the application

#### Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_hbase_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_hbase_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-hbase-integration/run_spark_hbase_integration_app.sh
```
