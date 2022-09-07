package repository

import akka.Done
import cats.implicits.catsSyntaxEitherId
import model.{BusinessImpactWithType, PaymentTypeDetail, RiskScore}
import org.slf4j.{Logger, LoggerFactory}
import play.api.db.slick.DatabaseConfigProvider
import request.RiskScoreReq
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import cats.implicits._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class BusinessImpactRepo @Inject()(dbConfigProvider: DatabaseConfigProvider)
                                    (implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[PostgresProfile]
  val logger: Logger = LoggerFactory.getLogger(getClass)

  import dbConfig._

  private class BusinessImpactTable(tag: Tag) extends Table[BusinessImpactWithType](tag, "BUSINESS_IMPACT") {

    def * = (merchantId, paymentProjection, paymentType) <> ((BusinessImpactWithType.apply _).tupled, BusinessImpactWithType.unapply)

    def paymentProjection =
      (paymentBlock, paymentInReview, paymentAllow) <> ((PaymentTypeDetail.apply _).tupled, PaymentTypeDetail.unapply)

    def merchantId = column[String]("MERCHANT_ID", O.Unique)

    def paymentType = column[String]("PAYMENT_TYPE")

    def paymentBlock = column[Int]("PAYMENT_BLOCK")

    def paymentInReview = column[Int]("PAYMENT_IN_REVIEW")

    def paymentAllow = column[Int]("PAYMENT_ALLOW")
  }

  val businessImpactTable  = TableQuery[BusinessImpactTable]

  def fetchBusinessDetail(merchantId: String, paymentType: String): Future[Either[String, BusinessImpactWithType]] = {
    val businessImpact = businessImpactTable.filter(col => (col.merchantId === merchantId && col.paymentType === paymentType))
    db.run(businessImpact.result).map { x =>
      Either.fromOption(x.headOption, s"No Business Impact found for merchantId: ${merchantId}")
    }
  }
}