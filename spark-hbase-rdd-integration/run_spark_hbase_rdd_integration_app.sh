#!/bin/bash

SCRIPT_NAME=`basename "$0"`

printf "\nRunning the <${SCRIPT_NAME}> script.\n"

spark-submit \
	--conf spark.app.name=SparkHbaseIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
 	--class com.ranga.spark.hbase.SparkHbaseIntegrationRDDApp \
	/apps/spark/spark-hbase-rdd-integration/spark-hbase-rdd-integration-1.0.0-SNAPSHOT.jar

printf "Finished <${SCRIPT_NAME}> script.\n"