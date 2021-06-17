#!/bin/bash
echo "Running <$0> script"

spark-submit \
  --master yarn \
  --deploy-mode client \
  --driver-memory 1g \
  --executor-memory 1g \
  --num-executors 2 \
  --executor-cores 3 \
  --class com.ranga.spark.s3.SparkS3IntegrationApp \
  /apps/spark/spark-s3-integration/spark-s3-integration-1.0.0-SNAPSHOT.jar

echo "Finished <$0> script"