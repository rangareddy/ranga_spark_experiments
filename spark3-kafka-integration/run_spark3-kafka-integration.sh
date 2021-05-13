echo "Running $0 script"

spark-submit \
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
