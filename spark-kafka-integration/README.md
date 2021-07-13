# Spark Kafka Integration

<div>
    <div style='float:left;padding: 10px;'>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/spark/spark_logo.png?raw=true" height="200" width="250"/>
    </div>
    <div style='float:left;padding: 10px;'>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/others/plus_logo.png?raw=true" height="200" width="250"/>
    </div>
    <div style='float:left;padding: 10px;'>
        <img src="https://github.com/rangareddy/ranga-logos/blob/main/frameworks/kafka/kafka_logo.png?raw=true" height="200" width="250"/>
    </div>
</div>
<br/><br/><br/><br/><br/><br/><br/><br/><br/>

## Prerequisites

* Spark Version : 2.4.0.7.1.6.0-297
* Java Version : 1.8
* Scala Version : 2.11.12

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

## Login to spark gateway node (for example mynode.host.com) and create the application deployment directory `/apps/spark/spark-kafka-integration`.
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
**Note:** Before building the application, update spark & other components library versions according to your cluster version.
```sh
$ mvn clean package -DskipTests
```

## Copy the `spark-kafka-integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_kafka_integration_app.sh` to spark gateway node `/apps/spark/spark-kafka-integration` directory.
```sh
$ scp target/spark-kafka-integration-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-kafka-integration
$ scp run_spark_kafka_integration_app.sh username@mynode.host.com:/apps/spark/spark-kafka-integration
```

## Login to spark gateway node (for example mynode.host.com) and run the application using `run_spark_kafka_integration_app.sh` shell script.

**Note(s):**
* Before running the application, check do you have proper permissions to run the application.
* Check is there any parameters needs to pass in `run_spark_kafka_integration_app.sh` shell script.

```sh
sh /apps/spark/spark-kafka-integration/run_spark_kafka_integration_app.sh
```