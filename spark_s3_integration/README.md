# Spark S3 Integration Example

<p>
  <img width="33%" height="250" src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Apache_Spark_logo.svg/800px-Apache_Spark_logo.svg.png">
  <img width="33%" height="250" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNXEcnxcKr8Ttd53Ibm5DO3Igsi0qWa8lHTav3fqugJyQ-w8E__29d2YtOuSKkykoQCD8&usqp=CAU">
  <img width="33%" height="250" src="https://fathomtech.io/blog/aws-s3-cloudfront/amazon-s3.png" style="float:right">
</p>

* [PySpark](https://github.com/rangareddy/ranga_spark_experiments/blob/master/spark_s3_integration/README.md#accessing-s3-from-pyspark)
* [Spark Scala/Java](https://github.com/rangareddy/ranga_spark_experiments/blob/master/spark_s3_integration/README.md#accessing-s3-from-spark-scalajava)

## Accessing S3 from PySpark

### Download the `spark_s3_integration` project
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark_s3_integration
```

### Create the application deployment directory in spark gateway node. for example `/apps/spark/spark_s3_integration`
```sh
$ ssh username@node2.host.com
$ mkdir -p /apps/spark/spark_s3_integration
$ chmod 755 /apps/spark/spark_s3_integration
```

### Copy the `spark_s3_integration.py` python file and run script `run_pyspark_s3_integration_example.sh` to spark gateway node `/apps/spark/spark_s3_integration` directory.
```sh
$ scp spark_s3_integration.py root@node2.host.com:/apps/spark/spark_s3_integration
$ scp run_pyspark_s3_integration_example.sh root@node2.host.com:/apps/spark/spark_s3_integration
```

## Run the `run_pyspark_s3_integration_example.sh` shell script.
Before running the shell script, update the following property values.
```sh
<AWS_ACCESS_KEY_ID> - yours aws access key
<AWS_SECRET_ACCESS_KEY> - yours aws secret access key
<BUCKET_NAME> - yours aws s3 bucket name
```
```sh
$ sh /apps/spark/spark_s3_integration/run_pyspark_s3_integration_example.sh
```

## Accessing S3 from Spark Scala/Java

### Download the `spark_s3_integration` project
```sh
$ git clone https://github.com/rangareddy/ranga_spark_experiments.git
$ cd ranga_spark_experiments/spark_s3_integration
```

### Build the `spark_s3_integration` application
```sh
$ mvn clean package
```

### Create the application deployment directory in spark gateway node. for example `/apps/spark/spark_s3_integration`
```sh
$ ssh username@node2.host.com
$ mkdir -p /apps/spark/spark_s3_integration
$ chmod 755 /apps/spark/spark_s3_integration
```

### Copy the `spark_s3_integration-1.0.0-SNAPSHOT.jar` uber jar and run script `run_spark_s3_integration_example.sh` to spark gateway node `/apps/spark/spark_s3_integration` directory
```sh
$ scp target/spark_s3_integration-1.0.0-SNAPSHOT.jar root@node2.host.com:/apps/spark/spark_s3_integration
$ scp run_spark_s3_integration_example.sh root@node2.host.com:/apps/spark/spark_s3_integration
```

## Run the `run_spark_s3_integration_example.sh` shell script.
Before running the shell script, update the following property values.
```sh
<AWS_ACCESS_KEY_ID> - yours aws access key
<AWS_SECRET_ACCESS_KEY> - yours aws secret access key
<BUCKET_NAME> - yours aws s3 bucket name
```
> If you are running application with java, you need to update the class name `com.ranga.spark.s3.SparkS3IntegrationJavaExample` in run_spark_s3_integration_example.sh.
Then run the following script.
```sh
sh /apps/spark/spark_s3_integration/run_spark_s3_integration_example.sh
```
