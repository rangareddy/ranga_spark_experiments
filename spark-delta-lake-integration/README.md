# Spark Delta Lake Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://docs.delta.io/latest/_static/delta-lake-logo.png" height="200" width="250"/>
</div>


## Prerequisites

|Component|Version|
|---------|-------|
|Scala|2.11.12|
|Java|1.8|
|Spark|2.4.0.7.1.6.0-297|
|Delta|0.6.1|


## Run interactively

### Spark Scala Shell

```sh
spark-shell --packages io.delta:delta-core_2.11:0.6.1 \ 
	--conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \ 
	--conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog"
```
#### Create a table

```scala
val data = spark.range(0, 5)
data.write.format("delta").save("/tmp/delta-table")
```

#### Read data

```scala
val df = spark.read.format("delta").load("/tmp/delta-table")
df.show()
```

### PySpark Shell

```sh
pyspark --packages io.delta:delta-core_2.11:0.6.1 \ 
	--conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \ 
	--conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog"
```

#### Create a table

```python
data = spark.range(0, 5)
data.write.format("delta").save("/tmp/delta-table")
```

#### Read data

```python
df = spark.read.format("delta").load("/tmp/delta-table")
df.show()
```

## Login to spark gateway node (for example mynode.host.com) and create the application deployment `/apps/spark/spark-delta-lake-integration` directory.

```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-delta-lake-integration
$ chmod 755 /apps/spark/spark-delta-lake-integration
```

## Download the `spark-delta-lake-integration` application.

```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-delta-lake-integration
```

## Build the `spark-delta-lake-integration` application.

### 1) Building the project using maven build tool

```sh
$ mvn clean package
```

### 2) Copy the `spark-delta-lake-integration-1.0.0-SNAPSHOT.jar` uber jar to spark gateway node `/apps/spark/spark-delta-lake-integration` directory.

```sh
$ scp target/spark-delta-lake-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-delta-lake-integration
```


## Copy the run script `run_spark_delta_lake_integration_app.sh` to spark gateway node `/apps/spark/spark-delta-lake-integration` directory.

```sh
$ scp run_spark_delta_lake_integration_app.sh username@mynode.host.com:/apps/spark/spark-delta-lake-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_delta_lake_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_delta_lake_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-delta-lake-integration/run_spark_delta_lake_integration_app.sh
```