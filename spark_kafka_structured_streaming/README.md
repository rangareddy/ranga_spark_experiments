# Spark Kafka Structured Streaming

## Exporting the KAFKA_HOME and JAVA_HOME

### HDP
```sh
export KAFKA_HOME=/usr/hdp/current/kafka-broker
export JAVA_HOME=/usr/jdk64/jdk1.8.0_112/
```

### CDP/CDH
```sh
export KAFKA_HOME=/opt/cloudera/parcels/CDH/lib/kafka/
export JAVA_HOME=/usr/java/jdk1.8.0_181-cloudera/
```

### Exporting the KAFKA_OPTS

#### Copy the kafka keytab to /etc/kafka

##### HDP
```sh

```

##### CDP/CDH
```
cp /var/run/cloudera-scm-agent/process/300-kafka-KAFKA_BROKER/kafka.keytab /etc/kafka/
```

#### Creating the kafka_client_jaas.conf

vi /etc/kafka/kafka_client_jaas.conf

```sh
KafkaClient {
    com.sun.security.auth.module.Krb5LoginModule required
    useKeyTab=true
    doNotPrompt=true
    keyTab="/etc/kafka/kafka.keytab"
    storeKey=true
    useTicketCache=false
    serviceName="kafka"
    principal="kafka/c4648-node2.coelab.cloudera.com@COELAB.CLOUDERA.COM";
};
```

#### Exporting the KAFKA_OPTS
```sh
export KAFKA_OPTS="-Djava.security.auth.login.config=/etc/kafka/kafka_client_jaas.conf"
```

## Creating the Kafka Topic

### Exporting the KAFKA_BROKERS and ZOOKEEPER_CONNECT

#### HDP
```sh
KAFKA_BROKERS=c2543-node2.coelab.cloudera.com:6667
ZOOKEEPER_CONNECT=c2543-node2.coelab.cloudera.com:2181,c2543-node3.coelab.cloudera.com:2181,c2543-node4.coelab.cloudera.com:2181
```

#### CDH/CDP
```
KAFKA_BROKERS=c4648-node2.coelab.cloudera.com:9092,c4648-node3.coelab.cloudera.com:9092,c4648-node4.coelab.cloudera.com:9092
ZOOKEEPER_CONNECT=c4648-node2.coelab.cloudera.com:2181,c4648-node3.coelab.cloudera.com:2181,c4648-node4.coelab.cloudera.com:2181
```

### Create topic
```sh
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper $ZOOKEEPER_CONNECT --replication-factor 1 --partitions 3 --topic json_topic
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper $ZOOKEEPER_CONNECT --replication-factor 1 --partitions 3 --topic avro_topic
```

### Creating the kafka_client.properties

vi /etc/kafka/kafka_client.properties

```sh
security.protocol=SASL_PLAINTEXT
sasl.kerberos.service.name=kafka
```

### Start Console Producer
```sh
$KAFKA_HOME/bin/kafka-console-producer.sh --broker-list $KAFKA_BROKERS --topic json_topic --producer.config /etc/kafka/kafka_client.properties
```

### Start Console Consumer
```sh
$KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server $KAFKA_BROKERS --topic json_topic --from-beginning --consumer.config /etc/kafka/kafka_client.properties
```
 
## Running Spark the Application
```sh
spark-submit \
        --master yarn \
        --deploy-mode client \
        --principal spark/c4648-node2.coelab.cloudera.com@COELAB.CLOUDERA.COM \
        --keytab /root/SparkExamples/spark_kafka_streaming/spark_on_yarn.keytab \
        --files /etc/kafka/kafka_client_jaas.conf,/root/SparkExamples/spark_kafka_streaming/spark_on_yarn.keytab  \
        --conf spark.executor.extraJavaOptions=-Djava.security.auth.login.config=/etc/kafka/kafka_client_jaas.conf  \
        --conf spark.driver.extraJavaOptions=-Djava.security.auth.login.config=/etc/kafka/kafka_client_jaas.conf  \
        --class com.ranga.spark.streaming.secure.SparkSecureKafkaStructuredStreamingJsonApp \
        ./SparkKafkaStructuredStreaming-1.0.0-SNAPSHOT.jar c4648-node2.coelab.cloudera.com:9092 json_topic avro_topic  
```