# spark-launcher-integration

## Prerequisites

## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-launcher-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-launcher-integration
$ chmod 755 /apps/spark/spark-launcher-integration
```

## Download the `spark-launcher-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-launcher-integration
```

## Build the `spark-launcher-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-launcher-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_launcher_integration_app.sh` to spark gateway node `/apps/spark/spark-launcher-integration` directory.
```sh
$ scp target/spark-launcher-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-launcher-integration
$ scp run_spark_launcher_integration_app.sh username@mynode.host.com:/apps/spark/spark-launcher-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_launcher_integration_app.sh` script.
**Note:** Before running the application, check you have application running permissions or not.
```sh
sh /apps/spark/spark-launcher-integration/run_spark_launcher_integration_app.sh
```