#!/bin/bash
echo "Running <$0> script"

BOOTSTRAP_SERVERS="localhost:9092"
GROUP_ID="test-group"
TOPICS="test-topic"
MARKER_FILE="/tmp/marker_file"
# hdfs dfs -touch /tmp/marker_file

spark-submit \
  --master yarn \
  --deploy-mode client \
  --driver-memory 2g \
  --executor-memory 2g \
  --num-executors 2 \
  --executor-cores 2 \
  --conf spark.dynamicAllocation.enabled=false \
  --name SparkStreamingKafkaGracefulShutdownApp \
  --class com.ranga.spark.streaming.shutdown.kafka.marker.SparkStreamingKafkaGracefulShutdownMarkerApp \
  /tmp/spark-streaming-graceful-shutdown-1.0.0-SNAPSHOT.jar "$BOOTSTRAP_SERVERS" "$GROUP_ID" "$TOPICS" "$MARKER_FILE"

echo "Finished <$0> script"