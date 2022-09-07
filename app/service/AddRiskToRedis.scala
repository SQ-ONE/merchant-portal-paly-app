package service

import akka.Done
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import redis.RedisClient

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import cats.implicits._

class AddRiskToRedis(system: ActorSystem)(implicit executionContext: ExecutionContext) {
  implicit val x = system
  val redisHost = ConfigFactory.load().getString("redis-host")
  val redisPort = ConfigFactory.load().getInt("redis-port")
  val redis = RedisClient(redisHost, redisPort)

  def publishMerchantRiskType(merchantId: String, riskType: String) = {
    redis.publish(merchantId, riskType).map { _=> Done.asRight[String]
    }.recover {
      case err => err.getMessage.asLeft[Done]
    }
  }
}
