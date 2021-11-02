#!/bin/bash

SCRIPT_NAME=`basename "$0"`

printf "\nRunning the <${SCRIPT_NAME}> script.\n"

if [ $# -lt 5 ]; then
    printf "Usage  : ${SCRIPT_NAME} <AWS_ACCESS_KEY_ID> <AWS_SECRET_ACCESS_KEY> <BUCKET_NAME> <PRINCIPAL> <KEYTAB>\n"
    exit 1
fi

AWS_ACCESS_KEY_ID=$1
AWS_SECRET_ACCESS_KEY=$2
BUCKET_NAME=$3
PRINCIPAL=$4
KEYTAB=$5

spark-submit \
	--conf spark.app.name=SparkS3Integration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
	--principal ${PRINCIPAL} \
	--keytab ${KEYTAB} \
 	--class com.ranga.spark.s3.SparkS3IntegrationApp \
	/apps/spark/spark-s3-integration/spark-s3-integration-1.0.0-SNAPSHOT.jar ${AWS_ACCESS_KEY_ID},${AWS_SECRET_ACCESS_KEY},${BUCKET_NAME}

printf "Finished <${SCRIPT_NAME}> script.\n"