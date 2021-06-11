#!/bin/bash

echo "Submitting the Spark Cassandra Integration Application"

spark-submit \
  --master yarn \
  --deploy-mode client \
  --driver-memory 1g \
  --executor-memory 2g \
  --executor-cores 2 \
  --num-executors 2 \
  --class com.ranga.spark.cassandra.SparkCassandraIntegrationApp \
  /apps/spark/spark_cassandra/spark_cassandra_integration-1.0.0-SNAPSHOT.jar

echo "Spark Cassandra Integration Application completed successfully."

