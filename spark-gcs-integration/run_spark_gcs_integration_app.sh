#!/bin/bash

SCRIPT_NAME=`basename "$0"`

printf "\nRunning the <${SCRIPT_NAME}> script.\n"

if [ $# -lt 5 ]; then
    printf "Usage  : ${SCRIPT_NAME} <PROJECT_ID> <BUCKET_NAME> <PRIVATE_KEY> <PRIVATE_KEY_ID> <CLIENT_EMAIL>\n"
    exit 1
fi

PROJECT_ID=$1
BUCKET_NAME=$2
PRIVATE_KEY=$3
PRIVATE_KEY_ID=$4
CLIENT_EMAIL=$5

spark-submit \
	--conf spark.app.name=SparkGcsIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
 	--class com.ranga.spark.gcs.SparkGcsIntegrationApp \
	/apps/spark/spark-gcs-integration/spark-gcs-integration-1.0.0-SNAPSHOT.jar ${PROJECT_ID},${BUCKET_NAME},${PRIVATE_KEY},${PRIVATE_KEY_ID},${CLIENT_EMAIL}

printf "Finished <${SCRIPT_NAME}> script.\n"