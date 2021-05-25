from __future__ import print_function
import sys

from pyspark.conf import SparkConf
from pyspark.sql import SparkSession
from pyspark.sql import Row

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage  : spark_s3_integration.py <AWS_ACCESS_KEY_ID> <AWS_SECRET_ACCESS_KEY>", file=sys.stderr)
        print("Example: spark_s3_integration.py ranga_aws_access_key ranga_aws_secret_key>", file=sys.stderr)
        exit(-1)

    awsAccessKey = sys.argv[1]
    awsSecretKey = sys.argv[2]

    conf = (
        SparkConf()
            .setAppName("PySpark S3 Integration Example")
            .set("spark.hadoop.fs.s3a.access.key", awsAccessKey)
            .set("spark.hadoop.fs.s3a.secret.key", awsSecretKey)
            .set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
            .set("spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version", "2")
            .set("spark.speculation", "false")
            .set("spark.hadoop.mapreduce.fileoutputcommitter.cleanup-failures.ignored", "true")
            .set("fs.s3a.experimental.input.fadvise", "random")
            .setIfMissing("spark.master", "local")
    )

    # Creating the SparkSession object
    spark = SparkSession.builder.config(conf=conf).getOrCreate()
    print("SparkSession Created successfully")

    Employee = Row("id", "name", "age", "salary")
    employee1 = Employee(1, "Ranga", 32, 245000.30)
    employee2 = Employee(2, "Nishanth", 2, 345000.10)
    employee3 = Employee(3, "Raja", 32, 245000.86)
    employee4 = Employee(4, "Mani", 14, 45000.00)

    employeeData = [employee1, employee2, employee3, employee4]
    employeeDF = spark.createDataFrame(employeeData)
    employeeDF.printSchema()
    employeeDF.show()

    # Define the s3 destination path
    bucketName="ranga-spark-s3-bkt"
    s3_dest_path = "s3a://" + bucketName + "/employees"

    # Write the data as Orc
    employeeOrcPath = s3_dest_path + "/employee_orc"
    employeeDF.write.mode("overwrite").format("orc").save(employeeOrcPath)

    # Read the employee orc data
    employeeOrcData = spark.read.format("orc").load(employeeOrcPath);
    employeeOrcData.printSchema()
    employeeOrcData.show()

    # Write the data as Parquet
    employeeParquetPath = s3_dest_path + "/employee_parquet"
    employeeOrcData.write.mode("overwrite").format("parquet").save(employeeParquetPath)

    spark.stop()
    print("SparkSession stopped")