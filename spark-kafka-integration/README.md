# Spark Kafka Integration

<div>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/kafka/kafka_logo.png?raw=true" height="200" width="250"/>
</div>


## Prerequisites

|Component|Version|
|---------|-------|
|Scala|2.11.12|
|Java|1.8|
|Spark|2.4.0.7.1.6.0-297|


## Creating a Kafka topic
```sh
$KAFAK_HOME/bin/kafka-topics.sh --create --bootstrap-server <BOOTSTRAP_SERVER_HOST>:<PORT> --replication-factor 3 --partitions 5 --topic <TOPIC_NAME>
```

## Describe a Kafka topic
```sh
$KAFAK_HOME/bin/kafka-topics.sh --describe --bootstrap-server <BOOTSTRAP_SERVER_HOST>:<PORT> --topic <TOPIC_NAME>
```

## Producing messages to Kafka topic
```sh
$KAFAK_HOME/bin/kafka-console-producer.sh --bootstrap-server <BOOTSTRAP_SERVER_HOST>:<PORT> --topic <TOPIC_NAME>
```

## Consuming messages from Kafka topic
```sh
$KAFAK_HOME/bin/kafka-console-consumer.sh --bootstrap-server <BOOTSTRAP_SERVER_HOST>:<PORT> --topic <TOPIC_NAME> --from-beginning
```

## Login to spark gateway node (for example mynode.host.com) and create the application deployment `/apps/spark/spark-kafka-integration` directory.

```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-kafka-integration
$ chmod 755 /apps/spark/spark-kafka-integration
```

## Download the `spark-kafka-integration` application.

```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-kafka-integration
```

## Build the `spark-kafka-integration` application.

### 1) Building the project using maven build tool

```sh
$ mvn clean package
```

### 2) Copy the `spark-kafka-integration-1.0.0-SNAPSHOT.jar` uber jar to spark gateway node `/apps/spark/spark-kafka-integration` directory.

```sh
$ scp target/spark-kafka-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-kafka-integration
```


## Copy the run script `run_spark_kafka_integration_app.sh` to spark gateway node `/apps/spark/spark-kafka-integration` directory.

```sh
$ scp run_spark_kafka_integration_app.sh username@mynode.host.com:/apps/spark/spark-kafka-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_kafka_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_kafka_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-kafka-integration/run_spark_kafka_integration_app.sh
```