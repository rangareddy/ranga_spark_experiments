#!/bin/bash
echo "Running <$0> script"

spark-submit \
  --master yarn \
  --deploy-mode client \
  --driver-memory 1g \
  --executor-memory 1g \
  --num-executors 2 \
  --executor-cores 3 \
  --class ${projectBuilder.fullClassName} \
  ${projectBuilder.jarPath}

echo "Finished <$0> script"