# CDH Spark HBase Integration using Spark Hortonworks Connector (SHC)

## Create and Insert the data to `employee` table in HBase

1). Login to HBase Shell
```sh
hbase shell
```
2). Create the HBase table(s) using following commands:
```sh
create 'employee', 'per', 'prof'
```
3). Insert the data 
```sh
put 'employee','1','per:name','Ranga'
put 'employee','1','per:age','32'
put 'employee','1','prof:designation','Software Engineer'
put 'employee','1','prof:salary','60000'

put 'employee','2','per:name','Nishanth'
put 'employee','2','per:age','3'
put 'employee','2','prof:designation','Junior Software Engineer'
put 'employee','2','prof:salary','80000'
```
4). Verify the HBase table data
```
scan 'employee'
ROW                                                              COLUMN+CELL
 1                                                               column=per:age, timestamp=1623301157496, value=32
 1                                                               column=per:name, timestamp=1623301157444, value=Ranga
 1                                                               column=prof:designation, timestamp=1623301157546, value=Software Engineer
 1                                                               column=prof:salary, timestamp=1623301157612, value=60000
 2                                                               column=per:age, timestamp=1623301157705, value=3
 2                                                               column=per:name, timestamp=1623301157671, value=Nishanth
 2                                                               column=prof:designation, timestamp=1623301157727, value=Junior Software Engineer
 2                                                               column=prof:salary, timestamp=1623301161097, value=80000
2 row(s)
Took 0.0991 seconds
```

## Building the shc-core project.

5). Add the **shc-core** dependency to the **pom.xml** file.
```xml
 <dependency>
    <groupId>com.hortonworks</groupId>
    <artifactId>shc-core</artifactId>
    <version>1.1.1-2.1-s_2.11</version>
</dependency>
```
Add hortonworks central repository to **pom.xml** file.
```xml
<repositories>

    <!-- Hortonworks repository -->
    <repository>
        <id>public</id>
        <url>http://repo.hortonworks.com/content/groups/public/</url>
    </repository>

</repositories>
```

6). Create the SparkSession
```
val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[4]")
val spark = SparkSession.builder.config(sparkConf).getOrCreate()
```

7). Define the both input and output tables HBase catalog.
```
     def inputCatalog = s"""{
                            |"table":{"namespace":"default", "name":"table1"},
                            |"rowkey":"key",
                            |"columns":{
                            |"key":{"cf":"rowkey", "col":"key", "type":"string"},
                            |"city":{"cf":"addr", "col":"city", "type":"string"},
                            |"state":{"cf":"addr", "col":"state", "type":"string"},
                            |"numb":{"cf":"order", "col":"numb", "type":"string"}
                            |}
                            |}""".stripMargin
    
     def outputCatalog = s"""{
                            |"table":{"namespace":"default", "name":"table2"},
                            |"rowkey":"key",
                            |"columns":{
                            |"key":{"cf":"rowkey", "col":"key", "type":"string"},
                            |"city":{"cf":"addr", "col":"city", "type":"string"},
                            |"state":{"cf":"addr", "col":"state", "type":"string"},
                            |"numb":{"cf":"order", "col":"numb", "type":"string"}
                            |}
                            |}""".stripMargin
```

8). Create the DataFrame using inputCatalog to read the data from HBase.

```
val inputTableDF = spark.read.options(Map(HBaseTableCatalog.tableCatalog->inputCatalog)).format(dataSourceFormat).load().cache()
```
9). Define useful method to print the DataFrame details.
```
    // Used to print the DataFrame information
    def printSchemaInfo(df: DataFrame) : Unit = {
        df.printSchema()
        df.show(false)
        println(s"Total records ${df.count()}")
    }
```
10). Print the input DataFrame details.
```
    printSchemaInfo(inputTableDF)
```

11). Create a temporary table and add the filter.
```
    inputTableDF.createOrReplaceTempView("table1_data")
    val outputTableDF = spark.sql("select * from table1_data where state='TX'")
```

12). Print the output DataFrame details.
```
   printSchemaInfo(outputTableDF)
```

13). Build the application
```shell script
mvn clean package
```

14). login to edge node and create the following folder
```shell script
mkdir -p /usr/apps/spark/spark-hbase
```

15). Deploy the application into edge node.
```shell script
scp -C target/spark-hbase-integration-1.0.0-SNAPSHOT.jar username@edgenode:/usr/apps/spark/spark-hbase/ 
``` 

16). Run the Spark Application using following command.
```shell script
spark-submit --class com.ranga.spark.hbase.SparkHBaseIntegrationApp \
  --master yarn \
  --deploy-mode cluster \
  --driver-memory 1g \
  --executor-memory 2g \
  --executor-cores 5 \
  --files /etc/hbase/conf/hbase-site.xml \
  /usr/apps/spark/spark-hbase/spark-hbase-integration-1.0.0-SNAPSHOT.jar
```

17). Output
```sh
root
 |-- key: string (nullable = true)
 |-- name: string (nullable = true)
 |-- age: string (nullable = true)
 |-- designation: string (nullable = true)
 |-- salary: string (nullable = true)

+---+--------+---+------------------------+------+
|key|name    |age|designation             |salary|
+---+--------+---+------------------------+------+
|1  |Ranga   |32 |Software Engineer       |60000 |
|2  |Nishanth|3  |Junior Software Engineer|80000 |
+---+--------+---+------------------------+------+
```

