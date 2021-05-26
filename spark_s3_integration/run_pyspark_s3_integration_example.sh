echo "Running $0 script"

spark-submit \
  --master yarn \
  --deploy-mode client \
  --driver-memory 1g \
  --executor-memory 1g \
  --num-executors 2 \
  /apps/spark/spark_s3_integration/spark_s3_integration.py <AWS_ACCESS_KEY_ID> <AWS_SECRET_ACCESS_KEY> <BUCKET_NAME>

echo "Finished $0 script"