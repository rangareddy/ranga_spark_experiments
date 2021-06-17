# spark-gcs-integration

## Prerequisites

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
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-gcs-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_gcs_integration_app.sh` to spark gateway node `/apps/spark/spark-gcs-integration` directory.
```sh
$ scp target/spark-gcs-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-gcs-integration
$ scp run_spark_gcs_integration_app.sh username@mynode.host.com:/apps/spark/spark-gcs-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_gcs_integration_app.sh` script.
**Note:** Before running the application, check you have application running permissions or not.
```sh
sh /apps/spark/spark-gcs-integration/run_spark_gcs_integration_app.sh
```