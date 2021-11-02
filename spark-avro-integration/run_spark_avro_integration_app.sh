#!/bin/bash

SCRIPT_NAME=`basename "$0"`

printf "\nRunning the <${SCRIPT_NAME}> script.\n"

if [ $# -lt 2 ]; then
    printf "Usage  : ${SCRIPT_NAME} <PRINCIPAL> <KEYTAB>\n"
    exit 1
fi

PRINCIPAL=$1
KEYTAB=$2

spark-submit \
	--conf spark.app.name=SparkAvroIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
	--principal ${PRINCIPAL} \
	--keytab ${KEYTAB} \
 	--class com.ranga.spark.avro.SparkAvroIntegrationApp \
	/apps/spark/spark-avro-integration/spark-avro-integration-1.0.0-SNAPSHOT.jar

printf "Finished <${SCRIPT_NAME}> script.\n"