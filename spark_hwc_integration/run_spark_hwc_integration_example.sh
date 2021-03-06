echo "Running $0 script"

spark-submit \
  --master yarn \
  --deploy-mode client \
  --executor-memory 1g \
  --num-executors 2 \
  --driver-memory 1g \
  --jars /opt/cloudera/parcels/CDH/jars/hive-warehouse-connector-assembly-*.jar \
  --conf spark.sql.hive.hiveserver2.jdbc.url='jdbc:hive2://localhost:10000' \
  --conf spark.sql.hive.hwc.execution.mode=spark \
  --conf spark.datasource.hive.warehouse.metastoreUri='thrift://localhost:9083' \
  --conf spark.datasource.hive.warehouse.load.staging.dir='/tmp' \
  --conf spark.datasource.hive.warehouse.user.name=hive \
  --conf spark.datasource.hive.warehouse.password=hive \
  --conf spark.datasource.hive.warehouse.smartExecution=false \
  --conf spark.datasource.hive.warehouse.read.via.llap=false \
  --conf spark.datasource.hive.warehouse.read.jdbc.mode=cluster \
  --conf spark.datasource.hive.warehouse.read.mode=DIRECT_READER_V2 \
  --conf spark.security.credentials.hiveserver2.enabled=false \
  --conf spark.sql.extensions=com.hortonworks.spark.sql.rule.Extensions \
  --class com.ranga.spark.hwc.SparkHWCJavaExample \
  /tmp/spark_hwc_integration-1.0.0-SNAPSHOT.jar

echo "Finished $0 script"