# Spark Hive Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/dbs/warehouse/hive/hive_logo.jpg?raw=true" height="200" width="250"/>
</div>


## Prerequisites

* Spark Version : 2.4.0.7.1.6.0-297
* Java Version : 1.8
* Scala Version : 2.11.12



## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-hive-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-hive-integration
$ chmod 755 /apps/spark/spark-hive-integration
```

## Download the `spark-hive-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-hive-integration
```

## Build the `spark-hive-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-hive-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_hive_integration_app.sh` to spark gateway node `/apps/spark/spark-hive-integration` directory.
```sh
$ scp target/spark-hive-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-hive-integration
$ scp run_spark_hive_integration_app.sh username@mynode.host.com:/apps/spark/spark-hive-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_hive_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_hive_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-hive-integration/run_spark_hive_integration_app.sh
```