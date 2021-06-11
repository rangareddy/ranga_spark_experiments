package com.ranga.spark.cassandra

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.SparkConf
import com.datastax.spark.connector.rdd.ReadConf
import org.apache.spark.sql.cassandra._

object SparkCassandraIntegrationApp extends App with Serializable {

  if (args.length < 1) {
    System.err.println("Usage   : SparkCassandraIntegrationApp <Cassandra_Host>")
    System.out.println("Example : SparkCassandraIntegrationApp localhost")
    System.exit(1)
  }

  println("Creating the SparkSession")
  val sparkConf = new SparkConf().setAppName("Spark Cassandra Integration").setIfMissing("spark.master", "local[4]")
  val cassandraHost = args(0)
  val cassandraPort = if(args.length > 1) args(1) else "9042"

  sparkConf.set("spark.cassandra.connection.host", cassandraHost)
  sparkConf.set("spark.cassandra.connection.port", cassandraPort)

  val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
  println("SparkSession created")

  val tableName = "employees"
  val keyspace = "ranga_keyspace"
  val cassandraFormat = "org.apache.spark.sql.cassandra"
  val options = Map( "keyspace" -> keyspace, "table" -> tableName)

  val employeeDF = spark.read.format(cassandraFormat).options(options).load()
  display(employeeDF)

  case class Employee(id:Long, name: String, age: Integer, salary: Float)
  import spark.implicits._
  var employeeDS = Seq(
    Employee(5L, "Yashwanth", 32, 80000.5f),
    Employee(6L, "Vasundra Reddy", 57, 180000.5f)
  ).toDS()

  employeeDS.write.mode(SaveMode.Append).format(cassandraFormat).options(options).save()

  val empDF = spark.read.format(cassandraFormat).options(options).load()
  display(empDF)

  spark.close()

  def display(dataFrame: DataFrame) = {
    dataFrame.printSchema()
    dataFrame.show(truncate=false)
  }
}

