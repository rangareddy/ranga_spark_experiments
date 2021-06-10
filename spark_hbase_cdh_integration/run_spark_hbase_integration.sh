#!/bin/bash

echo "Submitting the Spark HBase Integration Application"

spark-submit --master yarn --deploy-mode client \
  --driver-memory 1g \
  --executor-memory 2g \
  --executor-cores 5 \
  --num-executors 2 \
  --class com.ranga.spark.hbase.SparkHBaseIntegrationApp \
  /usr/apps/spark/spark-hbase/spark-hase-integration-1.0.0-SNAPSHOT.jar

echo "Spark HBase Integration Application completed successfully."

