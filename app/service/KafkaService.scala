package service

import akka.Done
import cats.implicits.catsSyntaxEitherId
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import cats.implicits._

import java.util.Properties
import scala.concurrent.Future
import scala.util.Try

class KafkaService {
  private val config          = ConfigFactory.load()
  private val broker          = config.getString("broker-url")
  private val topic           = config.getString("topic-name")

  def configureKafkaProducer():Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", broker)
    props.put("client.id", "producer")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props
  }

  def sendMessage(merchantId: String, listType: String): Future[Either[String, Done.type]] = {
    val props= configureKafkaProducer()
    val producer = new KafkaProducer[String, String](props)
    val t = System.currentTimeMillis()
    val data = new ProducerRecord[String, String](topic, merchantId, listType)
    producer.send(data)
    Future(Done.asRight[String])
  }
}
