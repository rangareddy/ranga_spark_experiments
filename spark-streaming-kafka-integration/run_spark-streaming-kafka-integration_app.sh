#!/bin/bash
echo "Running <$0> script"

spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --driver-memory 5g \
  --executor-memory 5g \
  --num-executors 7 \
  --executor-cores 5 \
  --conf spark.serializer=org.apache.spark.serializer.KryoSerializer \
  --conf spark.driver.memoryOverhead=1g \
  --conf spark.executor.memoryOverhead=1g \
  --conf spark.dynamicAllocation.enabled=false \
  --conf spark.streaming.backpressure.enabled=true \
  --conf spark.streaming.backpressure.initialRate=100 \
  --conf spark.streaming.kafka.maxRatePerPartition=100 \
  --conf spark.streaming.receiver.writeAheadLog.enable=true \
  --class com.ranga.spark.streaming.kafka.SparkStreamingKafkaIntegrationApp \
  /apps/spark/spark-streaming-kafka-integration/spark-streaming-kafka-integration-1.0.0-SNAPSHOT.jar

echo "Finished <$0> script"