# Spark Gcs Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/cloud/gcp/gcs_logo.png?raw=true" height="200" width="250"/>
</div>


## Prerequisites

|Component|Version|
|---------|-------|
|Scala|2.11.12|
|Java|1.8|
|Spark|2.4.0.7.1.6.0-297|
|Gcs Connector|2.1.2.7.1.6.0-297|




## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-gcs-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-gcs-integration
$ chmod 755 /apps/spark/spark-gcs-integration
```

## Download the `spark-gcs-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-gcs-integration
```

## Build the `spark-gcs-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.

### 1) Building the project using maven build tool

```sh
$ mvn clean package
```

### 2) Copy the `spark-gcs-integration-1.0.0-SNAPSHOT.jar` uber jar to spark gateway node `/apps/spark/spark-gcs-integration` directory.

```sh
$ scp target/spark-gcs-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-gcs-integration
```

### 1) Building the project using sbt build tool

```sh
$ sbt clean package
```

### 2) Copy the `spark-gcs-integration-1.0.0-SNAPSHOT.jar` uber jar to spark gateway node `/apps/spark/spark-gcs-integration` directory.

```sh
$ scp target/2.11/spark-gcs-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-gcs-integration
```

## Copy the run script `run_spark_gcs_integration_app.sh` to spark gateway node `/apps/spark/spark-gcs-integration` directory.

```sh
$ scp run_spark_gcs_integration_app.sh username@mynode.host.com:/apps/spark/spark-gcs-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_gcs_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_gcs_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-gcs-integration/run_spark_gcs_integration_app.sh
```