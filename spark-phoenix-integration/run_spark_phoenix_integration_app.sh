#!/bin/bash

SCRIPT_NAME=`basename "$0"`

printf "\nRunning the <${SCRIPT_NAME}> script.\n"

if [ $# -lt 4 ]; then
    printf "Usage  : ${SCRIPT_NAME} <PHOENIX_SERVER_URL> <TABLE_NAME> <PRINCIPAL> <KEYTAB>\n"
    exit 1
fi

PHOENIX_SERVER_URL=$1
TABLE_NAME=$2
PRINCIPAL=$3
KEYTAB=$4

spark-submit \
	--conf spark.app.name=SparkPhoenixIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
	--principal ${PRINCIPAL} \
	--keytab ${KEYTAB} \
 	--class com.ranga.spark.phoenix.SparkPhoenixIntegrationApp \
	/apps/spark/spark-phoenix-integration/spark-phoenix-integration-1.0.0-SNAPSHOT.jar ${PHOENIX_SERVER_URL},${TABLE_NAME}

printf "Finished <${SCRIPT_NAME}> script.\n"