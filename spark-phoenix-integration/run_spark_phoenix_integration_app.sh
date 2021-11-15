#!/bin/bash

SCRIPT_NAME=`basename "$0"`

printf "\nRunning the <${SCRIPT_NAME}> script.\n"

if [ $# -lt 2 ]; then
    printf "Usage  : ${SCRIPT_NAME} <PHOENIX_SERVER_URL> <TABLE_NAME>\n"
    exit 1
fi

PHOENIX_SERVER_URL=$1
TABLE_NAME=$2

spark-submit \
	--conf spark.app.name=SparkPhoenixIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
 	--class com.ranga.spark.phoenix.SparkPhoenixIntegrationApp \
	/apps/spark/spark-phoenix-integration/spark-phoenix-integration-1.0.0-SNAPSHOT.jar ${PHOENIX_SERVER_URL},${TABLE_NAME}

printf "Finished <${SCRIPT_NAME}> script.\n"