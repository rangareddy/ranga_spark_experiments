#!/bin/bash

echo ""
echo "Running the <$0> script"
echo ""

if [ $# -lt 4 ]; then
    echo "Usage   : $0 <PHOENIX_SERVER_URL> <TABLE_NAME> <PRINCIPAL> <KEYTAB>"
    echo " "
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
	/apps/spark/spark-phoenix-integration/spark-phoenix-integration-1.0.0-SNAPSHOT.jar

echo "Finished <$0> script"