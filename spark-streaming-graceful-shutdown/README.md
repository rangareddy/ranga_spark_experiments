# spark-streaming-graceful-shutdown

## Prerequisites

* Spark Version :   2.4.7
* Scala Version :   2.11.12
* Java Version  :   1.8
* Kafka Version :   2.5.0

## Step 1: Build and Deploy the Application

### 1. Download the "spark-streaming-graceful-shutdown" application.

```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark-streaming-graceful-shutdown
```

### 2. Build the `spark-streaming-graceful-shutdown` application.

> Before building the application, ensure that you update the Spark and other component's library versions according to your cluster version.

```sh
$ mvn clean package -DskipTests
```

### 3. Log in to the Spark gateway node, such as "mynode.host.com", and create the application deployment directory.

```sh
$ ssh username@mynode.host.com
$ mkdir -p /apps/spark/spark-streaming-graceful-shutdown
$ chmod 755 /apps/spark/spark-streaming-graceful-shutdown
```

### 4. Copy the necessary JAR files and shell scripts to run the application.

```sh
$ scp target/spark-streaming-graceful-shutdown-1.0.0-SNAPSHOT.jar username@mynode.host.com:/apps/spark/spark-streaming-graceful-shutdown
```

```shell
$ scp run_spark_streaming_socket_graceful_shutdown_app.sh username@mynode.host.com:/apps/spark/spark-streaming-graceful-shutdown
$ scp run_spark_streaming_kafka_graceful_shutdown_app.sh username@mynode.host.com:/apps/spark/spark-streaming-graceful-shutdown
```

## Step2 - Run the Spark Streaming application using the Socket source

### 1. Launch the SocketStream by running the following command in the shell:

```shell
$ nc -lk 9999
```

### 2. Log in to the Spark gateway node, such as "mynode.host.com", and execute the application.

Before running the application, please ensure the following:

1. Verify that you have the necessary permissions to execute the script.
2. Update the values of HOST_NAME and PORT in the script to match your specific configuration.

Once you have confirmed these details, you can proceed to run the application.

```sh
sh /apps/spark/spark-streaming-graceful-shutdown/run_spark_streaming_socket_graceful_shutdown_app.sh
```

The script will prompt you to enter an option based on the desired approach:

> If you want to use the marker filesystem approach, you should provide the HDFS file system path as the input. For example, you can enter "/tmp/SparkStreamingKafkaGracefulShutdownMarkerApp/marker_file". It is important to use a unique path for each application.
> If you want to use the HTTP service approach, you should provide the Jetty service port as the input. For example, you can enter "3443". Ensure that you use a unique port number for each application.

After entering the required information, the script will proceed accordingly.

## Step3 - Run Spark Streaming application using the Kafka source.

### 1. Create the Kafka topic and verify the kafka topic is created or not.

```sh
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic test-topic
kafka-topics --list --bootstrap-server localhost:9092
```

### 2. Produce messages to the Kafka topic.

```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic test-topic
```

(or)

```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic test-topic < my_file.txt
```

### 3. Consume messages from the Kafka topic.

```sh
kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic --from-beginning
```

### 4. Login to spark gateway node (for example mynode.host.com) and run the application.

Before running the application, please verify the following:

1. Ensure that you have the necessary permissions to run the script. If you encounter any permission issues, contact your system administrator or check the file permissions to ensure that you have execute permissions.
2. Update the BOOTSTRAP_SERVERS value in the script. The BOOTSTRAP_SERVERS variable should be set to the list of Kafka broker endpoints in the format <host1:port1>,<host2:port2>,.... Replace <host1:port1>,<host2:port2>,... with the actual Kafka broker endpoints for your cluster. For example:

```shell
BOOTSTRAP_SERVERS="kafka1:9092,kafka2:9092,kafka3:9092"
```

3. Ensure that the Kafka broker endpoints are correct and accessible from the machine where you are running the script.

```sh
sh /apps/spark/spark-streaming-graceful-shutdown/run_spark_streaming_kafka_graceful_shutdown_app.sh
```

The script will prompt you to enter an option based on the desired approach:

> If you want to use the marker filesystem approach, you should provide the HDFS file system path as the input. For example, you can enter "/tmp/SparkStreamingKafkaGracefulShutdownMarkerApp/marker_file". It is important to use a unique path for each application.
> If you want to use the HTTP service approach, you should provide the Jetty service port as the input. For example, you can enter "3443". Ensure that you use a unique port number for each application.

After entering the required information, the script will proceed accordingly.

## 4. Stop the Spark Streaming Job gracefully.

### 1. Stop the Spark Streaming Job using Shutdown Hook Approach

a) There are multiple ways to send the shutdown hook signal. One of the mostly used approach is by pressing the Ctrl+C.

### 2. Stop the Spark Streaming Job using Shutdown Signal Approach

a) Go to the Spark UI and find out the driver hostname from the Executors tab.

    -   Access the Spark UI by navigating to the URL of your Spark cluster (e.g., http://spark-master:4040).
    -   Click on the Executors tab in the Spark UI.
    -   Look for the row that represents the driver, and note down the hostname of the driver.

b) Login to the Driver host and find out the Application Master process id.

    -   Open a terminal or SSH into the host machine where the Spark driver is running.
    -   Run the following command to find the process id (PID) of the Application Master:

    ```shell
    jps -m | grep "spark.deploy.master.Master"
    ```
    This command will display the process id and other information of the Spark Application Master. Note down the process id.

c) Kill the process id.

    -   Run the following command to kill the Application Master process:

    ```sh
    kill <process_id>`
    ```

    Replace <process_id> with the actual process id you obtained in the previous step.

### 3. Stop the Spark Streaming Job using Shutdown Marker Approach

a) Collect the marker file path specified while running the Spark application. For example, marker file path is 
`/tmp/SparkStreamingKafkaGracefulShutdownMarkerApp/marker_file`.

b) Create an hdfs directory

```shell
# hdfs dfs -mkdir -p /tmp/SparkStreamingKafkaGracefulShutdownMarkerApp
```

c) Create a marker file under an HDFS directory

```shell
# hdfs dfs -touch /tmp/SparkStreamingKafkaGracefulShutdownMarkerApp/marker_file
```

d) Once the application is successfully stopped then marker file will be deleted automatically.

### 4. Stop the Spark Streaming Job using Shutdown HTTP Approach

a) Collect the HTTP port specified while running the Spark application. For example the http port is `3443`.
b) To collect the HTTP service path for your Spark application, you can follow these steps:

    -   Open the Spark UI by accessing the URL provided by your Spark cluster (e.g., http://<spark_master_host>:4040).
    -   Go to the "Executors" tab in the Spark UI.
    -   Find the driver executor and note the value under the "Host" column. This is the driver host.
    -   Once you have the driver host, you can construct the HTTP service path using the format http://<driver_host>:<port>/shutdown/<app_name>.

c) Run the above command using browser url or curl command. Here i am running the command from browser.

`http://localhost:3443/shutdown/SparkStreamingKafkaGracefulShutdownHttpApp`

> You need to replace localhost with driver_host and correct application name.
> Ensure that the Spark application is running and the HTTP service is set up correctly for the shutdown to be executed successfully.

You will see the following output after successful shutdown.

```shell
The Spark Streaming application has been successfully stopped.
```
