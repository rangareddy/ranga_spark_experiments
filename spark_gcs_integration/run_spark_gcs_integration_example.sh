echo "Running $0 script"

spark-submit \
  --master yarn \
  --deploy-mode client \
  --driver-memory 1g \
  --executor-memory 1g \
  --num-executors 2 \
  --class com.ranga.spark.gcs.SparkGCSIntegrationExample \
  /apps/spark/spark_gcs_integration/spark_gcs_integration-1.0.0-SNAPSHOT.jar <AWS_ACCESS_KEY_ID> <AWS_SECRET_ACCESS_KEY> <BUCKET_NAME>

echo "Finished $0 script"