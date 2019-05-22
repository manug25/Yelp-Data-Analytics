package com.manu.yelp.stream

import com.datastax.spark.connector.cql.CassandraConnectorConf
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.streaming.{ProcessingTime, Trigger}

object StreamData {

  def main(args: Array[String]): Unit = {

    val host = "localhost"
    val clusterName = "DevCluster"
    val keySpace = "yelp_data"



    @transient lazy val conf: SparkConf = new SparkConf()
      .setAppName("Structured Streaming from Kafka to Cassandra")
      .set("spark.cassandra.connection.host", "localhost")
      .set("spark.sql.streaming.checkpointLocation", "checkpoint")

    val spark = SparkSession.builder()
      .appName("Yelp Data Stream")
      .master("local[*]")
      .config(conf)
      .getOrCreate()

   // spark.setCassandraConf(clusterName,CassandraConnectorConf.ConnectionHostParam.option(host))


    /*if(args.length < 4) {
      System.err.println("Usage: KafkaProducer <BrokerList> <topic> <messgaePerSec> <wordsPermessage>")
      System.exit(1)
    }*/

    StreamConfigProvider.setConfig("/streamConfig.properties")

    import spark.implicits._

    val businessStreamDF = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092,localhost:9093")   // comma separated list of broker:host
      .option("subscribe", "yelp-business")    // comma separated list of topics
      .option("startingOffsets", "earliest")
      .option("max.poll.records", 10)
      .load()
      .select(from_json(col("value").cast("string"), SchemaProvider.businessSchema)).as("data")
      .filter(col("data.jsontostructs(CAST(value AS STRING))").isNotNull)
      .select("data.*")


    /*val query = businessStreamDF.writeStream
      .format("console")
      .outputMode("append")
      .option("truncate",false)
      .option("checkpointLocation","/media/manu/coding/coding/checkpointLocation/")
      .trigger(Trigger.ProcessingTime("5 seconds"))
      .start()*/

    val toCassandraBusinessTable = businessStreamDF
        .writeStream
        .foreachBatch {(batchDF: DataFrame, batchId: Long) =>

          println("Writing to Cassandra")

          batchDF.write
            .cassandraFormat("business", keySpace)
            .option("cluster", clusterName)
            .mode("append")
            .save()
        }
        .outputMode("update")
        .start()

    toCassandraBusinessTable.awaitTermination()

  }

}
