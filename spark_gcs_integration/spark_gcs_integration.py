from __future__ import print_function
import sys

from pyspark.sql import SparkSession
from pyspark.sql import Row

if __name__ == "__main__":
    if len(sys.argv) != 5:
        print("Usage  : spark_gcs_integration.py <PROJECT_ID> <BUCKET_NAME> <PRIVATE_KEY> <PRIVATE_KEY_ID> <CLIENT_EMAIL>", file=sys.stderr)
        print("Example: spark_gcs_integration.py ranga-gcp-spark-project ranga-spark-gcp-bkt ranga_private_key ranga_private_key_id rangareddy@project.iam.gserviceaccount.com", file=sys.stderr)
        exit(-1)

    projectId = sys.argv[1]
    bucketName = sys.argv[2]
    privateKey = sys.argv[3]
    privateKeyId = sys.argv[4]
    clientEmail = sys.argv[5]
    appName = "PySpark GCS Integration Example"

    # Creating the SparkSession object
    spark = SparkSession.appName(appName).builder.config(conf=conf).getOrCreate()

    # GCS settings
    conf = spark.sparkContext._jsc.hadoopConfiguration()
    conf.set("fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
    conf.set("fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
    conf.set("fs.gs.auth.service.account.enable", "true")
    conf.set("fs.gs.project.id", projectId)
    conf.set("fs.gs.auth.service.account.private.key", privateKey)
    conf.set("fs.gs.auth.service.account.private.key.id", privateKeyId)
    conf.set("fs.gs.auth.service.account.email", clientEmail)

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

    # Define the gs destination path
    gcs_dest_path = "gs://" + bucketName + "/employees"
    print("gc destination path "+gcs_dest_path)

    # Write the data as Orc
    employeeOrcPath = gcs_dest_path + "/employee_orc"
    employeeDF.write.mode("overwrite").format("orc").save(employeeOrcPath)

    # Read the employee orc data
    employeeOrcData = spark.read.format("orc").load(employeeOrcPath);
    employeeOrcData.printSchema()
    employeeOrcData.show()

    # Write the data as Parquet
    employeeParquetPath = gcs_dest_path + "/employee_parquet"
    employeeOrcData.write.mode("overwrite").format("parquet").save(employeeParquetPath)

    spark.stop()
    print("SparkSession stopped")