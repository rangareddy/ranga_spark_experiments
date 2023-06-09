# spark-streaming-graceful-shutdown

## Prerequisites

* Spark Version :   2.4.7
* Scala Version :   2.11.12
* Java Version  :   1.8

## Download the `spark-streaming-graceful-shutdown` application.

```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-streaming-graceful-shutdown
```

## Build the `spark-streaming-graceful-shutdown` application.

**Note:** Before building the application, update spark & other component's library versions according to your cluster version.

```sh
$ mvn clean package -DskipTests
```

## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-streaming-graceful-shutdown`.

```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-streaming-graceful-shutdown
$ chmod 755 /apps/spark/spark-streaming-graceful-shutdown
```

## Copy the `spark-streaming-graceful-shutdown-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_streaming_graceful_shutdown_app.sh` to spark gateway node `/apps/spark/spark-streaming-graceful-shutdown` directory.

```sh
$ scp target/spark-streaming-graceful-shutdown-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-streaming-graceful-shutdown
$ scp run_spark_streaming_graceful_shutdown_app.sh username@mynode.host.com:/apps/spark/spark-streaming-graceful-shutdown
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_streaming_graceful_shutdown_app.sh` script.

**Note:** Before running the application, check you have application running permissions or not.

```sh
sh /apps/spark/spark-streaming-graceful-shutdown/run_spark_streaming_graceful_shutdown_app.sh
```

To stop the job, create an empty file with the name `marker_file` and place it under hdfs `/tmp/streaming/graceful_shutdown_app` directory.

```shell
#  > /tmp/marker_file
# hdfs dfs -put /tmp/marker_file /tmp/streaming/graceful_shutdown_app
```

## Kafka with Spark Streaming Graceful Shutdown

```sh
kafka-topics.sh --create \
--zookeeper localhost:2181 \
--replication-factor 1 \
--partitions 1 \
--topic test
```

```sh
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic test-topic
```

```sh
kafka-topics --list --bootstrap-server localhost:9092
```

```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic test-topic
```

```sh
kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic --from-beginning
```