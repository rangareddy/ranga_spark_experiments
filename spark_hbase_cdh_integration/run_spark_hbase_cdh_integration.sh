#!/bin/bash

echo "Submitting the Spark HBase Integration Application"

sudo -u spark spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --driver-memory 1g \
  --executor-memory 2g \
  --executor-cores 2 \
  --class com.ranga.spark.hbase.SparkHBaseIntegrationApp \
  /apps/spark/spark-hbase/spark_hbase_cdh_integration-1.0.0-SNAPSHOT.jar

echo "Spark HBase Integration Application completed successfully."

