# Spark3 Kafka Integration in CDP

## Project setup

### Download the spark3-kafka-integration project
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
```
### Change the directory to spark3-kafka-integration
```
$ cd ranga_spark_experiments/spark3-kafka-integration
```
### Build the project
```
$ mvn clean package -DskipTests
```

### Copy the spark3-kafka-integration-1.0.0-SNAPSHOT.jar to gateway/edge node to /tmp directory
```
$ scp target/spark3-kafka-integration-1.0.0-SNAPSHOT.jar root@node1.hadoop.com:/tmp
```

## Create the Kafka Topic and Produce the messages

### kinit as a kafka user
Copy the **kafka.keytab** file to temp location for example **/tmp/kafka.keytab**
```sh
$ sudo find /var/run/cloudera-scm-agent/process/ -not -empty | grep 'kafka.keytab'
/var/run/cloudera-scm-agent/process/1546340514-kafka-KAFKA_BROKER/kafka.keytab

$ cp /var/run/cloudera-scm-agent/process/1546340514-kafka-KAFKA_BROKER/kafka.keytab /tmp/
```
kinit as kafka user

```sh
$ kinit -kt /tmp/kafka.keytab kafka/node1.hadoop.com@HADOOP.COM
```
### Create and list the kafka topics
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
  --executor-memory 1g \
  --num-executors 2 \
  --driver-memory 1g \
  --files /tmp/kafka_client_jaas.conf \
  --conf "spark.driver.extraJavaOptions=-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf" \
  --conf "spark.executor.extraJavaOptions=-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf" \
  --class com.ranga.spark.kafka.SparkKafkaSecureStructuredStreaming \
  /tmp/spark3-kafka-integration-1.0.0-SNAPSHOT.jar node1.hadoop.com:9092 SASL_PLAINTEXT KafkaWordCount
```
