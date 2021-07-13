#!/bin/bash

echo ""
echo "Running the <$0> script"
echo ""

if [ $# -lt 2 ]; then
    echo "Usage   : $0 <PRINCIPAL> <KEYTAB>"
    echo " "
    exit 1
fi

PRINCIPAL=$1
KEYTAB=$2

spark-submit \
	--conf spark.app.name=SparkHelloWorldIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
	--principal ${PRINCIPAL} \
	--keytab ${KEYTAB} \
 	--class com.ranga.spark.hello.world.SparkHelloWorldIntegrationApp \
	/apps/spark/spark-hello-world-integration/spark-hello-world-integration-1.0.0-SNAPSHOT.jar

echo "Finished <$0> script"