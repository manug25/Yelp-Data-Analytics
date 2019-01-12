import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}


object StreamData {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("Yelp Data Stream").getOrCreate()

    if(args.length < 4) {
      System.err.println("Usage: KafkaProducer <BrokerList> <topic> <messgaePerSec> <wordsPermessage>")
      System.exit(1)
    }

    StreamConfigProvider.setConfig("/streamConfig.properties")

    val businessSchema = StructType(Array(
      StructField("business_id",StringType, true),
      StructField("name",StringType,true),
      StructField("neighborhood", StringType, true),
      StructField("address", StringType, true),
      StructField("city", StringType, true),
      StructField("state", StringType, true),
      StructField("postal code", StringType,true)))

    import spark.implicits._
    val schema = new StructType()
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
      .option("kafka.bootstrap.servers", "getKafkaHost")
      .option("subscribe","businessTopic")
      .option("startingOffsets", "earliest")
      .option("max.poll.records", 10)
      .option("failOnDataLoss", false)
      .load()
      //.select(from_json(col("value").cast("string"), schema))

    val userStreamDF = spark
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
      .load()

    val query = businessStreamDF.writeStream
      .foreach(new HBaseForeachWriter[Row] {
        override val tableName: String = "businessData"
        override val hbaseConfResources: Seq[String] = Seq("core-site.xml", "hbase-site.xml")

        override def toPut(record: Row): Put = {
          val key = businessStreamDF(colName = "business_id").toString()
          val columnFamilyName: String = "id"
          val columnName: String = businessStreamDF.col("business_id").toString()
          val columnValue = ""

          val p = new Put(Bytes.toBytes(key))
          p.addColumn(Bytes.toBytes(columnFamilyName),
            Bytes.toBytes(columnName),
            Bytes.toBytes(columnValue))

          p
        }
      })


  }

  def getS() = {

    val businessSchema = StructType(Array(
      StructField("business_id",StringType, true),
      StructField("name",StringType,true),
      StructField("neighborhood", StringType, true),
      StructField("address", StringType, true),
      StructField("city", StringType, true),
      StructField("state", StringType, true),
      StructField("postal code", StringType,true)
    ))

  }
}
