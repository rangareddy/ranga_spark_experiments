#!/bin/bash

echo ""
echo "Running the <$0> script"
echo ""

if [ $# -lt 5 ]; then
    echo "Usage   : $0 <HIVE_SERVER2_JDBC_URL> <HIVE_METASTORE_URI> <HIVE_SERVER2_AUTH_KERBEROS_PRINCIPAL> <PRINCIPAL> <KEYTAB>"
    echo " "
    exit 1
fi

HIVE_SERVER2_JDBC_URL=$1
HIVE_METASTORE_URI=$2
HIVE_SERVER2_AUTH_KERBEROS_PRINCIPAL=$3
PRINCIPAL=$4
KEYTAB=$5

spark-submit \
	--conf spark.app.name=SparkHwcIntegration \
	--conf spark.master=yarn \
	--conf spark.submit.deployMode=client \
	--conf spark.driver.memory=1g \
	--conf spark.executor.memory=1g \
	--conf spark.driver.cores=1 \
	--conf spark.executor.cores=3 \
	--conf spark.executor.instances=2 \
	--conf spark.sql.hive.hwc.execution.mode=spark \
	--conf spark.datasource.hive.warehouse.load.staging.dir=/tmp \
	--conf spark.datasource.hive.warehouse.read.via.llap=false \
	--conf spark.datasource.hive.warehouse.read.jdbc.mode=cluster \
	--conf spark.datasource.hive.warehouse.read.mode=DIRECT_READER_V1 \
	--conf spark.kryo.registrator=com.qubole.spark.hiveacid.util.HiveAcidKyroRegistrator \
	--conf spark.sql.extensions=com.hortonworks.spark.sql.rule.Extensions \
	--conf spark.sql.hive.hiveserver2.jdbc.url=${HIVE_SERVER2_JDBC_URL} \
	--conf spark.hadoop.hive.metastore.uris=thrift://${HIVE_METASTORE_URI}:9083 \
	--conf spark.security.credentials.hiveserver2.enabled=false \
	--conf spark.sql.hive.hiveserver2.jdbc.url.principal=${HIVE_SERVER2_AUTH_KERBEROS_PRINCIPAL} \
	--principal ${PRINCIPAL} \
	--keytab ${KEYTAB} \
 	--class com.ranga.spark.hwc.SparkHwcIntegrationApp \
	/apps/spark/spark-hwc-integration/spark-hwc-integration-1.0.0-SNAPSHOT.jar

echo "Finished <$0> script"