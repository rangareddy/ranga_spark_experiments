# Spark S3 Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/cloud/aws/amazon_s3_logo.png?raw=true" height="200" width="250"/>
</div>


## Prerequisites

* Spark Version : 2.4.0.7.1.6.0-297
* Aws Java Sdk Bundle Version : 1.11.375
* Hadoop Aws Version : 3.1.1.7.1.6.0-297
* Java Version : 1.8
* Scala Version : 2.11.12



## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-s3-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-s3-integration
$ chmod 755 /apps/spark/spark-s3-integration
```

## Download the `spark-s3-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-s3-integration
```

## Build the `spark-s3-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-s3-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_s3_integration_app.sh` to spark gateway node `/apps/spark/spark-s3-integration` directory.
```sh
$ scp target/spark-s3-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-s3-integration
$ scp run_spark_s3_integration_app.sh username@mynode.host.com:/apps/spark/spark-s3-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_s3_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_s3_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-s3-integration/run_spark_s3_integration_app.sh
```