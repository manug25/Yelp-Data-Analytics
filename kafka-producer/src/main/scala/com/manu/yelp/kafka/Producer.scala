package com.manu.yelp.kafka

import java.io.FileNotFoundException
import java.util.concurrent.Future
import java.util.{HashMap, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.spark.sql.SparkSession

import scala.io.Source

object Producer {

  var producer: KafkaProducer[String,String] = _

  def main(args: Array[String]): Unit = {

    /*if(args.length < 1) {
      println("Number of args is : " + args.length)
      System.err.println("Usage: spark-submit --class <class-Name> <jar-path>")
      System.exit(1)
    }*/
    val spark = SparkSession.builder().appName("Yelp Data Kafka Producer").master("local[*]").getOrCreate()

    println("Started Producer")

    ConfigProvider.setConfig("/kafkaConfig.properties")

    val props = new Properties()

    props.put("bootstrap.servers", ConfigProvider.bootstrapServers)
    props.put("acks", ConfigProvider.acks)
    //props.put("retries", 1)
    //props.put("batch.size", ConfigProvider.batchSize)
    //props.put("linger.ms", 1)
    //props.put("buffer.memory", 33554432)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    producer = new KafkaProducer[String,String](props)

//Sending business data to topic businessTopic
    while(true){
      try{
        for(line <- Source.fromFile(ConfigProvider.dataPath+"/business.json").getLines()){
          println("Now sending records" + line + "/n")
          produce("yelp-business",line)
        }
        Thread.sleep(1000)
      } catch {
        case f: FileNotFoundException => println("No files available. Waiting for new records")
        case e:Exception => e.printStackTrace()
      }
    }
  }

  def produce(topic:String,data:String): Future[RecordMetadata] ={

    producer.send(new ProducerRecord[String,String](topic,data))
  }
}
