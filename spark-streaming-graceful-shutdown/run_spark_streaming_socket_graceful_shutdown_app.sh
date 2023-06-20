#!/bin/bash

#!/bin/bash
echo "Running <$0> script"

HOST_NAME="localhost"
PORT=9999
SOCKET_PARAMS="$HOST_NAME $PORT"
OTHER_PARAMETERS=""
CLASS_NAME=""
BASE_PACKAGE_NAME="com.ranga.spark.streaming.shutdown.socket"

echo "Select a shutdown mechanism:"
echo "1. Shutdown Hook"
echo "2. Shutdown Signal"
echo "3. Shutdown Marker file system"
echo "4. Shutdown HTTP service"

read -p "Enter your choice: " choice

case $choice in
    1)
        echo "Shutdown Hook invoked"
        CLASS_NAME="$BASE_PACKAGE_NAME.hook.SparkStreamingSocketGracefulShutdownHookApp"
        ;;
    2)
        echo "Shutdown Signal invoked"
        CLASS_NAME="$BASE_PACKAGE_NAME.signal.SparkStreamingSocketGracefulShutdownSignalApp"
        ;;
    3)
        echo "Shutdown Marker file system invoked"
        CLASS_NAME="$BASE_PACKAGE_NAME.marker.SparkStreamingSocketGracefulShutdownMarkerApp"
        read -p "Enter marker file path(example /tmp/myapp/marker): " marker_file
        OTHER_PARAMETERS="$marker_file"
        ;;
    4)
        echo "Shutdown HTTP service invoked"
        CLASS_NAME="$BASE_PACKAGE_NAME.http.SparkStreamingSocketGracefulShutdownHttpApp"
        read -p "Enter jetty port(example 3443): " jetty_port
        OTHER_PARAMETERS="$jetty_port"
        ;;
    *)
        echo "Invalid choice. Exiting..."
        exit 0
        ;;
esac

# Extract the class name
# shellcheck disable=SC2001
APP_NAME=$(echo "$CLASS_NAME" | sed 's/.*\.//')

spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --driver-memory 2g \
  --executor-memory 2g \
  --num-executors 2 \
  --executor-cores 2 \
  --conf spark.dynamicAllocation.enabled=false \
  --name "$APP_NAME" \
  --class "$CLASS_NAME" \
  /apps/spark/spark-streaming-graceful-shutdown/spark-streaming-graceful-shutdown-1.0.0-SNAPSHOT.jar "$SOCKET_PARAMS" "$OTHER_PARAMETERS"

echo "Finished <$0> script"