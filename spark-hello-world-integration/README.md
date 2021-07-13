# Spark Hello World Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
</div>


## Prerequisites

* Spark Version : 2.4.0.7.1.6.0-297
* Java Version : 1.8
* Scala Version : 2.11.12



## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-hello-world-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-hello-world-integration
$ chmod 755 /apps/spark/spark-hello-world-integration
```

## Download the `spark-hello-world-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-hello-world-integration
```

## Build the `spark-hello-world-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-hello-world-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_hello_world_integration_app.sh` to spark gateway node `/apps/spark/spark-hello-world-integration` directory.
```sh
$ scp target/spark-hello-world-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-hello-world-integration
$ scp run_spark_hello_world_integration_app.sh username@mynode.host.com:/apps/spark/spark-hello-world-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_hello_world_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_hello_world_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-hello-world-integration/run_spark_hello_world_integration_app.sh
```