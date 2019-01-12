

import java.util.concurrent.ExecutorService
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}
import org.apache.hadoop.hbase.security.User
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.spark.sql.ForeachWriter

trait HBaseForeachWriter[RECORD] extends ForeachWriter[RECORD] {

    val tableName: String
    val hbaseConfResources: Seq[String]

    def pool: Option[ExecutorService] = None

    def user: Option[User] = None

    private var hTable: Table = _
    private var connection: Connection = _


    override def open(partitionId: Long, version: Long): Boolean = {
      connection = createConnection()
      hTable = getHTable(connection)
      true
    }

    def createConnection(): Connection = {
      val hbaseConfig = HBaseConfiguration.create()
      hbaseConfResources.foreach(hbaseConfig.addResource)
      ConnectionFactory.createConnection(hbaseConfig, pool.orNull, user.orNull)

    }

    def getHTable(connection: Connection): Table = {
      connection.getTable(TableName.valueOf(tableName))
    }

    override def process(record: RECORD): Unit = {
      val put = toPut(record)
      hTable.put(put)
    }

    override def close(errorOrNull: Throwable): Unit = {
      hTable.close()
      connection.close()
    }

    def toPut(record: RECORD): Put

}
