# spark-streaming-kafka-integration

## Prerequisites
* Spark Version :   2.3.0
* Scala Version :   2.11.8
* Java Version  :   1.8
* Kafka Version :   1.0.0

## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-streaming-kafka-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-streaming-kafka-integration
$ chmod 755 /apps/spark/spark-streaming-kafka-integration
```

## Download the `spark-streaming-kafka-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-streaming-kafka-integration
```

## Build the `spark-streaming-kafka-integration` application.
**Note:** Before building the application, update spark & other component's library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-streaming-kafka-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_streaming_kafka_integration.sh` to spark gateway node `/apps/spark/spark-streaming-kafka-integration` directory.
```sh
$ scp target/spark-streaming-kafka-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-streaming-kafka-integration
$ scp run_spark-streaming-kafka-integration_app.sh username@mynode.host.com:/apps/spark/spark-streaming-kafka-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark-streaming-kafka-integration_app.sh` script.
**Note:** Before running the application, check you have application running permissions or not.
```sh
sh /apps/spark/spark-streaming-kafka-integration/run_spark-streaming-kafka-integration_app.sh
```