package com.ranga.spark.parquet

import java.io.File
import java.util.concurrent.TimeUnit

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{lit, rand}
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

/* Ranga Reddy created on 13/07/20 */
object ReadWriteParquetUsingStructured extends App {

    val logger = LoggerFactory.getLogger(this.getClass)
    val appName = this.getClass.getSimpleName().replace("$", "")
    val username = System.getProperty("user.home")
    val eventLogPath = username+"/applicationHistory"
    val eventLogFile = new File(eventLogPath)
    if(!eventLogFile.exists()) {
        eventLogFile.mkdirs()
    }
    val outputParquetFile = "structured_output_parquet_data"

    val conf = new SparkConf().setAppName(appName)
        .setIfMissing("spark.master", "local[2]")
        .setIfMissing("spark.eventLog.enabled", "true")
        .setIfMissing("spark.eventLog.dir", eventLogPath)
        .setIfMissing("spark.history.fs.logDirectory", eventLogPath)

    val sc = new SparkContext(conf)
    val session = SparkSession.builder.config(conf).getOrCreate()
    val parquetFile = "parquet_data"
    val schema = new StructType().add("lsoa_code",StringType).add("borough",StringType).add("major_category",StringType).add("minor_category",StringType).add("year",IntegerType).add("month",IntegerType).add("day",IntegerType).add("hour",IntegerType)

    def writeParquet(session: SparkSession) = {
        logger.info("Writing Parquet file")

        val inputDF = session.readStream.schema(schema).parquet(parquetFile)


       // logger.info("Input data count "+inputDF.count())

        val newInputDF = inputDF.withColumn("day", (rand()*16).cast("int")).withColumn("hour", lit(0))

        val yearDF = newInputDF.withColumnRenamed("year", "original_year").
            withColumn("year", lit(2014)).
            withColumnRenamed("month", "original_month").
            withColumn("month", lit(5)).
            withColumnRenamed("day", "original_day").
            withColumn("day", lit(14))

        val startTime = System.currentTimeMillis()

        val outputDF = yearDF.writeStream.format("parquet").partitionBy("year", "month", "day", "hour").queryName("Parquet File Write")
            .option("checkpointLocation", eventLogPath)
            .option("path",outputParquetFile).trigger(Trigger.Once()).start()
        outputDF.awaitTermination()

        printLogDuration("writting file", startTime)
    }

    def printLogDuration(msgStr:String, startTime: Long) = {
        val duration = System.currentTimeMillis() - startTime
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
        val msg = "Time taken for "+msgStr +" is "
        if(seconds > 0) {
            val mins = TimeUnit.SECONDS.toMinutes(seconds)
            if(mins > 0) {
                logger.info(msg+ mins +" mins")
            } else {
                logger.info(msg+ seconds +" sec")
            }
        } else {
            logger.info(msg+ duration +" ms")
        }
    }

    def readParquet(session: SparkSession) = {
        logger.info("Reading Parquet file")
        var startTime = System.currentTimeMillis()
        val parquestDF = session.read.schema(schema).parquet(outputParquetFile)
        val count = parquestDF.count()

        printLogDuration("count parquet file", startTime)
        logger.info("DF Count "+count)

        startTime = System.currentTimeMillis()
        val empDF2 = session.read.schema(schema).parquet(outputParquetFile+"/year=2014/month=5/day=14")
        val count2 = empDF2.count()

        printLogDuration("count by parquet file", startTime)
        logger.info("DF count by partition "+count2)
    }

    writeParquet(session)
    readParquet(session)

    println("Done")
    Thread.sleep(100000)
}