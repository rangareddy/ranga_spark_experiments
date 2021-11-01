# Spark Avro Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/file_formats/avro_logo.png?raw=true" height="200" width="250"/>
</div>


## Prerequisites

|Component|Version|
|---------|-------|
|Scala|2.11.12|
|Java|1.8|
|Spark|2.4.0.7.1.6.0-297|




## Login to spark gateway node (for example mynode.host.com) and create the application deployment `/apps/spark/spark-avro-integration` directory.

```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-avro-integration
$ chmod 755 /apps/spark/spark-avro-integration
```

## Download the `spark-avro-integration` application.

```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-avro-integration
```

## Build the `spark-avro-integration` application.

### 1) Building the project using maven build tool

```sh
$ mvn clean package
```

### 2) Copy the `spark-avro-integration-1.0.0-SNAPSHOT.jar` uber jar to spark gateway node `/apps/spark/spark-avro-integration` directory.

```sh
$ scp target/spark-avro-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-avro-integration
```


## Copy the run script `run_spark_avro_integration_app.sh` to spark gateway node `/apps/spark/spark-avro-integration` directory.

```sh
$ scp run_spark_avro_integration_app.sh username@mynode.host.com:/apps/spark/spark-avro-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_avro_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_avro_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-avro-integration/run_spark_avro_integration_app.sh
```