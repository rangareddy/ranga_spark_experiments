sudo -u spark spark-submit \
  --master yarn \
  --deploy-mode client \
  --driver-memory 1g \
  --executor-memory 1g \
  --num-executors 2 \
  --jars /opt/cloudera/parcels/CDH/jars/aws-java-sdk-bundle-1.11.375.jar,/opt/cloudera/parcels/CDH/lib/hadoop/hadoop-aws-3.1.1.7.1.6.0-297.jar \
  --class com.ranga.spark.s3.SparkS3IntegrationExample \
  /tmp/spark_s3_integration-1.0.0-SNAPSHOT.jar