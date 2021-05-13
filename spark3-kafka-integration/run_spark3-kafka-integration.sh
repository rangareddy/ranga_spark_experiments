echo "Running $0 script"

spark-submit \
  --master local[*] \
  --deploy-mode client \
  --executor-memory 1g \
  --num-executors 1 \
  --driver-memory 1g \
  --files /tmp/kafka_client_jaas.conf \
  --driver-java-options "-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf" \
  --conf "spark.executor.extraJavaOptions=-Djava.security.auth.login.config=/tmp/kafka_client_jaas.conf" \
  --class com.ranga.spark.kafka.SparkKafkaSecureStructuredStreaming \
  /tmp/SparkCDPHWCExample-1.0.0-SNAPSHOT.jar
