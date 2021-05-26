# Spark S3 Integration Example

* [PySpark](https://github.com/rangareddy/ranga_spark_experiments/tree/master/spark_s3_integration#pyspark-project-setup)
* [Spark Scala/Java](https://github.com/rangareddy/ranga_spark_experiments/tree/master/spark_s3_integration#spark-scalajava-project-setup)

## Pyspark Project setup

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

### Copy the `spark_s3_integration.py` python file and run script `run_pyspark_s3_integration_example.sh` to spark gateway node `/apps/spark/spark_s3_integration` directory
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
sh /apps/spark/spark_s3_integration/run_pyspark_s3_integration_example.sh
```

## Spark Scala/Java Project setup

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

## Run the shell script.
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