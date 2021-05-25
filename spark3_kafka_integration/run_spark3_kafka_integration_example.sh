echo "Running $0 script"

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
  /tmp/spark3_kafka_integration-1.0.0-SNAPSHOT.jar node1.hadoop.com:9092 SASL_PLAINTEXT KafkaWordCount
  
echo "Finished $0 script"
