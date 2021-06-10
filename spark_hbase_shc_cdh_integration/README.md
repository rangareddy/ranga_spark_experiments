# Spark HBase Integration using Spark Hortonworks Connector (SHC)

## Creating Tables in HBase
1). Login to HBase Shell
```
hbase shell
```
2). Create the HBase table(s) using following commands:
```hbase
create 'table1', {NAME=>'addr'}, {NAME=>'order'}

put   'table1',  'jsmith',  'addr:state', 'TN'
put   'table1',  'jsmith',  'addr:city', 'nashville'
put   'table1',  'jsmith',  'order:numb', '1234'
put   'table1',  'tsimmons',  'addr:city', 'dallas'
put   'table1',  'tsimmons',  'addr:state', 'TX'
put   'table1',  'tsimmons', 'order:numb', '1831'
put   'table1',  'jsmith',  'addr:state', 'CO'
put   'table1',  'jsmith',  'addr:city', 'denver'
put   'table1',  'jsmith',  'order:numb', '6666'
put   'table1',  'njones',  'addr:state', 'TX'
put   'table1',  'njones',  'addr:city', 'miami'
put   'table1',  'njones',  'order:numb', '5555'
put   'table1',  'amiller', 'addr:state', 'TX'
put   'table1',  'amiller', 'addr:city', 'dallas'
put   'table1',  'amiller', 'order:numb', '9986'

create 'table2', {NAME=>'addr'}, {NAME=>'order'}
```

3). Verify the HBase table data
```
scan 'table1'
scan 'table2'
```

4). Check **hbase-site.xml** file is present in Spark configuration directory(/usr/hdp/current/spark2-client/conf/) or not. 
If it is not present copy the **hbase-site.xml** and place it in under **spark/conf** directory.

```
# cp /usr/hdp/current/hbase-client/conf/hbase-site.xml /usr/hdp/current/spark2-client/conf/
```

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
  --executor-memory 512MB \
  --driver-memory 1g \
  --executor-memory 2g \
  --executor-cores 1 \
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

