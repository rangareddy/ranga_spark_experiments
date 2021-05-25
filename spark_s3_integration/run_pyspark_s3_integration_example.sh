echo "Running $0 script"

spark-submit \
  --master yarn \
  --deploy-mode client \
  --driver-memory 1g \
  --executor-memory 1g \
  --num-executors 2 \
  --jars /opt/cloudera/parcels/CDH/jars/aws-java-sdk-bundle-1.11.375.jar,/opt/cloudera/parcels/CDH/lib/hadoop/hadoop-aws-3.1.1.7.1.6.0-297.jar \
  /tmp/spark_s3_integration.py <AWS_ACCESS_KEY_ID> <AWS_SECRET_ACCESS_KEY>

echo "Finished $0 script"