# spark-kafka-structured-streaming-integration

## Prerequisites

## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-kafka-structured-streaming-integration`.
```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-kafka-structured-streaming-integration
$ chmod 755 /apps/spark/spark-kafka-structured-streaming-integration
```

## Download the `spark-kafka-structured-streaming-integration` application.
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-kafka-structured-streaming-integration
```

## Build the `spark-kafka-structured-streaming-integration` application.
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-kafka-structured-streaming-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_kafka_structured_streaming_integration_app.sh` to spark gateway node `/apps/spark/spark-kafka-structured-streaming-integration` directory.
```sh
$ scp target/spark-kafka-structured-streaming-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-kafka-structured-streaming-integration
$ scp run_spark_kafka_structured_streaming_integration_app.sh username@mynode.host.com:/apps/spark/spark-kafka-structured-streaming-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_kafka_structured_streaming_integration_app.sh` script.
**Note:** Before running the application, check you have application running permissions or not.
```sh
sh /apps/spark/spark-kafka-structured-streaming-integration/run_spark_kafka_structured_streaming_integration_app.sh
```