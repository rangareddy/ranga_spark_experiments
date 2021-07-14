#!/bin/bash

echo ""
echo "Running the <$0> script"
echo ""

if [ $# -lt 7 ]; then
    echo "Usage   : $0 <PROJECT_ID> <BUCKET_NAME> <PRIVATE_KEY> <PRIVATE_KEY_ID> <CLIENT_EMAIL> <PRINCIPAL> <KEYTAB>"
    echo " "
    exit 1
fi

PROJECT_ID=$1
BUCKET_NAME=$2
PRIVATE_KEY=$3
PRIVATE_KEY_ID=$4
CLIENT_EMAIL=$5
PRINCIPAL=$6
KEYTAB=$7

spark-submit \
	--conf spark.app.name=SparkGcsIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
	--principal ${PRINCIPAL} \
	--keytab ${KEYTAB} \
 	--class com.ranga.spark.gcs.SparkGcsIntegrationApp \
	/apps/spark/spark-gcs-integration/spark-gcs-integration-1.0.0-SNAPSHOT.jar ${PROJECT_ID},${BUCKET_NAME},${PRIVATE_KEY},${PRIVATE_KEY_ID},${CLIENT_EMAIL}

echo "Finished <$0> script"