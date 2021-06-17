# spark-hwc-integration

## Prerequisites

## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-hwc-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-hwc-integration
$ chmod 755 /apps/spark/spark-hwc-integration
```

## Download the `spark-hwc-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-hwc-integration
```

## Build the `spark-hwc-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-hwc-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_hwc_integration_app.sh` to spark gateway node `/apps/spark/spark-hwc-integration` directory.
```sh
$ scp target/spark-hwc-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-hwc-integration
$ scp run_spark_hwc_integration_app.sh username@mynode.host.com:/apps/spark/spark-hwc-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_hwc_integration_app.sh` script.
**Note:** Before running the application, check you have application running permissions or not.
```sh
sh /apps/spark/spark-hwc-integration/run_spark_hwc_integration_app.sh
```