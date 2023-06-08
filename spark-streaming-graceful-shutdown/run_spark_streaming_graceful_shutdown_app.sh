#!/bin/bash
echo "Running <$0> script"

HOST_NAME=$(hostname -f)
PORT=9999

spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --driver-memory 2g \
  --executor-memory 2g \
  --num-executors 2 \
  --executor-cores 2 \
  --conf spark.yarn.maxAppAttempts=1 \
  --conf spark.streaming.receiver.writeAheadLog.enable=false \
  --conf spark.dynamicAllocation.enabled=false \
  --name SparkStreamingGracefulShutdownApp \
  --class com.ranga.spark.streaming.shutdown.socket.marker.SparkStreamingGracefulShutdownMarkerApp \
  /apps/spark/spark-streaming-graceful-shutdown/spark-streaming-graceful-shutdown-1.0.0-SNAPSHOT.jar "$HOST_NAME" "$PORT"

echo "Finished <$0> script"