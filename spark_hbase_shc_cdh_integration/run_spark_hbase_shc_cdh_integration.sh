#!/bin/bash

echo "Submitting the SparkHBaseIntegrationApp"

spark-submit --class com.ranga.spark.hbase.SparkHBaseIntegrationApp \
  --master yarn \
  --deploy-mode cluster \
  --driver-memory 1g \
  --executor-memory 2g \
  --executor-cores 5 \
  /apps/spark/spark-hbase/spark_hbase_shc_integration-1.0.0-SNAPSHOT.jar

echo "SparkHBaseIntegrationApp submitted successfully."

