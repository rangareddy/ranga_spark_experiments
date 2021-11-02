#!/bin/bash

SCRIPT_NAME=`basename "$0"`

printf "\nRunning the <${SCRIPT_NAME}> script.\n"

if [ $# -lt 3 ]; then
    printf "Usage  : ${SCRIPT_NAME} <KUDU_MASTER> <PRINCIPAL> <KEYTAB>\n"
    exit 1
fi

KUDU_MASTER=$1
PRINCIPAL=$2
KEYTAB=$3

spark-submit \
	--conf spark.app.name=SparkKuduIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
	--principal ${PRINCIPAL} \
	--keytab ${KEYTAB} \
 	--class com.ranga.spark.kudu.SparkKuduIntegrationApp \
	/apps/spark/spark-kudu-integration/spark-kudu-integration-1.0.0-SNAPSHOT.jar ${KUDU_MASTER}

printf "Finished <${SCRIPT_NAME}> script.\n"