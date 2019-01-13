package com.manu.yelp.stream

import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.functions._


object StreamData {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("Yelp Data Stream").getOrCreate()

    /*if(args.length < 4) {
      System.err.println("Usage: KafkaProducer <BrokerList> <topic> <messgaePerSec> <wordsPermessage>")
      System.exit(1)
    }*/

    StreamConfigProvider.setConfig("/streamConfig.properties")

    import spark.implicits._
    val businessSchema = new StructType()
      .add($"business_id".string)
      .add($"name".string)
      .add($"neighborhood".string)
      .add($"address".string)
      .add($"city".string)
      .add($"state".string)
      .add($"postal code".string)

    val businessStreamDF = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092,localhost:9093")   // comma separated list of broker:host
      .option("subscribe", "businessTopic")    // comma separated list of topics
      .option("startingOffsets", "earliest")
      .option("max.poll.records", 10)
      .load()
      .select(from_json('business_id, businessSchema) as 'businessId)

    /*val userStreamDF = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "getKafkaHost")
      .option("subscribe","userTopic")
      .load()

    val checkinStreamDF = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "getKafkaHost")
      .option("subscribe","checkinTopic")
      .load()

    val photoStreamDF = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "getKafkaHost")
      .option("subscribe","photoTopic")
      .load()

    val reviewStreamDF = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "getKafkaHost")
      .option("subscribe","reviewTopic")
      .load()

    val tipStreamDF = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "getKafkaHost")
      .option("subscribe","tipsTopic")
      .load()*/

    /*val query = businessStreamDF.writeStream
        .format("Console")
        .outputMode("append")
      .option("checkpointLocation","/media/manu/Coding/Coding/resources")
        .start()*/

    val query = businessStreamDF.writeStream
      .foreach(new HBaseForeachWriter[Row] {
        override val tableName: String = "businessData"
        override val hbaseConfResources: Seq[String] = Seq("core-site.xml", "hbase-site.xml")

        override def toPut(record: Row): Put = {
          val key = businessStreamDF(colName = "business_id").toString()
          val columnFamilyName: String = "data"
          val columnName: String = businessStreamDF.col("business_id").toString()
          val columnValue = businessStreamDF("business_id").toString()

          val p = new Put(Bytes.toBytes(key))
          p.addColumn(Bytes.toBytes(columnFamilyName),
            Bytes.toBytes(columnName),
            Bytes.toBytes(columnValue))
          p
        }
      })
      .outputMode("append")
      .start()

    query.awaitTermination()

  }

  /*def getSchema(schemaName: String) = {

    schemaName match {
      case "businessSchema" => val businessSchema = StructType(Array(
        StructField("business_id",StringType, true),
        StructField("name",StringType,true),
        StructField("neighborhood", StringType, true),
        StructField("address", StringType, true),
        StructField("city", StringType, true),
        StructField("state", StringType, true),
        StructField("postal code", StringType,true),
        StructField("stars", FloatType, true),
        StructField("review_count", IntegerType, true)
      ))
    }
  }*/
}
