package repository

import akka.Done
import cats.implicits.catsSyntaxEitherId
import model.RiskScore
import org.slf4j.{Logger, LoggerFactory}
import play.api.db.slick.DatabaseConfigProvider
import request.RiskScoreReq
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import cats.implicits._
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RiskScoreSettingRepo @Inject()(dbConfigProvider: DatabaseConfigProvider)
                             (implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[PostgresProfile]
  val logger: Logger = LoggerFactory.getLogger(getClass)

  import dbConfig._

  private class RiskScoreSettingTable(tag: Tag) extends Table[RiskScore](tag, "RISK_SCORE") {

    def * = (merchantId, riskType) <> ((RiskScore.apply _).tupled, RiskScore.unapply)

    def merchantId = column[String]("MERCHANT_ID", O.Unique)

    def riskType = column[String]("RISK_TYPE")
  }

  private val riskScoreSettingTable = TableQuery[RiskScoreSettingTable]

  def insertRiskScore(riskScoreReq: RiskScoreReq): Future[Either[String, Done]] = {
    val insertMessage = riskScoreSettingTable += model.RiskScore(riskScoreReq.merchantId, riskScoreReq.riskType)
    db.run(insertMessage).map { _ =>
      Done.asRight[String]
    }.recover {
      case ex => ex.getMessage.asLeft[Done]
    }
  }

  def fetchRiskScore(merchantId: String): Future[Either[String, String]] = {
    val fetchMessage = riskScoreSettingTable.filter(_.merchantId === merchantId).map(_.riskType)
    db.run(fetchMessage.result).map { x =>
      Either.fromOption(x.headOption, s"No Risk Type found for merchantId: ${merchantId}")
    }
  }

}
