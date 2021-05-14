# Spark3 Kafka Integration

### Creating the Kafka Topic

#### kinit as a kafka user
Copy the **kafka.keytab** file to temp location for example **/tmp/kafka.keytab**
```sh
 cp /var/run/cloudera-scm-agent/process/1546340514-kafka-KAFKA_BROKER/kafka.keytab /tmp/
```
kinit as kafka user

```sh
kinit -kt /tmp/kafka.keytab kafka/node1.hadoop.com@HADOOP.COM
```
#### create and list the kafka topics
```sh
kafka-topics --create --zookeeper node1.hadoop.com:2181,node2.hadoop.com:2181 --replication-factor 1 --partitions 2 --topic KafkaWordCount
kafka-topics --list --zookeeper node1.hadoop.com:2181,node2.hadoop.com:2181
```
In CDP,
```sh
kafka-topics --create --zookeeper node1.hadoop.com:2181/kafka,node2.hadoop.com:2181/kafka --replication-factor 1 --partitions 2 --topic KafkaWordCount
kafka-topics --list --zookeeper node1.hadoop.com:2181/kafka,node2.hadoop.com:2181/kafka
```
### Create client.properites for passing the consumer and producer config
```sh
vi /tmp/client.properties
```
```sh
security.protocol=SASL_PLAINTEXT
sasl.kerberos.service.name=kafka
```
### Create Kafka Client jaas file
```sh
vi /tmp/kafka_client_jaas.conf
```

```sh
KafkaClient {
    com.sun.security.auth.module.Krb5LoginModule required
    useKeyTab=true
    keyTab="/tmp/kafka.keytab"
    storeKey=true
    useTicketCache=false
    serviceName="kafka"
    principal="kafka/node1.hadoop.com@HADOOP.COM";
};
```

### Export the Kafka opts
```sh
export KAFKA_PLAIN_PARAMS="-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf"
export KAFKA_OPTS="$KAFKA_PLAIN_PARAMS $KAFKA_OPTS"
```

### Produce the kafka message
```sh
kafka-console-producer --broker-list node1.hadoop.com:9092 --topic KafkaWordCount --producer.config /tmp/client.properties
```

## Submit the Spark Application using spark3-submit command
```sh
spark3-submit \
  --master yarn \
  --deploy-mode client \
  --executor-memory 4g \
  --num-executors 2 \
  --driver-memory 4g \
  --executor-cores 5
  --files /tmp/kafka_client_jaas.conf \
  --driver-java-options "-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf" \
  --conf "spark.executor.extraJavaOptions=-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf" \
  --class com.ranga.spark.kafka.SparkKafkaSecureStructuredStreaming \
  /tmp/SparkCDPHWCExample-1.0.0-SNAPSHOT.jar
```
