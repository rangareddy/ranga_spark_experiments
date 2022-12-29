package com.ranga.spark.hbase

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.shaded.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.security.UserGroupInformation
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import java.io.IOException
import java.util.Base64

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 12/29/2022
 */

object SparkHbaseIntegrationSecureApp extends Serializable {

    @transient private lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {

        val appName = "Spark Hbase Integration"

        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        val tableName = "employees"

        // Configure HBase for reading
        val conf = HBaseConfiguration.create()
        conf.set(TableInputFormat.INPUT_TABLE, tableName)

        //conf.set("hbase.zookeeper.quorum", "c3543-node3.coelab.cloudera.com")
        //conf.set("hbase.zookeeper.property.clientPort", "2181")
        //conf.set("zookeeper.znode.parent", "/hbase")

        conf.set("hadoop.security.authentication", "kerberos")
        conf.set("hbase.security.authentication", "kerberos")

        System.setProperty("java.security.krb5.conf", "/etc/krb5.conf")
        System.setProperty("sun.security.krb5.debug", "true")

        val principal = "hbaseuser@EXAMPLE.COM"
        val keytabLocation = "/home/user/hbase-client.keytab"

        UserGroupInformation.setConfiguration(conf)
        UserGroupInformation.loginUserFromKeytab(principal, keytabLocation)

        val scan = new Scan
        val columnFamily = Bytes.toBytes("e")
        scan.addFamily(columnFamily)

        val scanString = convertScanToString(scan)
        conf.set(org.apache.hadoop.hbase.mapreduce.TableInputFormat.SCAN, scanString);

        // Load an RDD of (row key, row Result) tuples from the table
        val hBaseRDD = spark.sparkContext.newAPIHadoopRDD(conf, classOf[TableInputFormat],
            classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
            classOf[org.apache.hadoop.hbase.client.Result])

        val count = hBaseRDD.count()
        println("HBase records count : "+count)

        // transform (ImmutableBytesWritable, Result) tuples into an RDD of Employee
        val resultRDD = hBaseRDD.map(tuple => convertToEmployee(tuple._2, columnFamily))
        resultRDD.collect().foreach(println)

        logger.info("<Spark Hbase Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    private def convertToEmployee(result: Result, columnFamily: Array[Byte]) = {
        val id = Bytes.toString(result.getRow()).split(" ")(0).toLong
        val name = Bytes.toString(result.getValue(columnFamily, Bytes.toBytes("name")))
        val age = Bytes.toString(result.getValue(columnFamily, Bytes.toBytes("age"))).toInt
        val salary = Bytes.toString(result.getValue(columnFamily, Bytes.toBytes("salary"))).toFloat
        Employee(id, name, age, salary)
    }

    @throws[IOException]
    def convertScanToString(scan: Scan): String = {
        val protobufUtil = ProtobufUtil.toScan(scan)
        Base64.getEncoder.encodeToString(protobufUtil.toByteArray)
    }
}