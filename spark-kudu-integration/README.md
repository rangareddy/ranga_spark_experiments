# spark-kudu-integration

## Prerequisites

## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-kudu-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-kudu-integration
$ chmod 755 /apps/spark/spark-kudu-integration
```

## Download the `spark-kudu-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-kudu-integration
```

## Build the `spark-kudu-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-kudu-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_kudu_integration_app.sh` to spark gateway node `/apps/spark/spark-kudu-integration` directory.
```sh
$ scp target/spark-kudu-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-kudu-integration
$ scp run_spark_kudu_integration_app.sh username@mynode.host.com:/apps/spark/spark-kudu-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_kudu_integration_app.sh` script.
**Note:** Before running the application, check you have application running permissions or not.
```sh
sh /apps/spark/spark-kudu-integration/run_spark_kudu_integration_app.sh
```