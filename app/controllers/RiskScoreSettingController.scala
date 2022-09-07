package controllers

import model.RiskScore

import javax.inject._
import play.api._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import _root_.request.RiskScoreReq
import akka.Done
import cats.data.EitherT
import cats.implicits._
import repository.{BusinessImpactRepo, RiskScoreSettingRepo}
import response.BusinessImpactResp
import service.{AddRiskToRedis, KafkaService}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class RiskScoreSettingController @Inject()(val controllerComponents: ControllerComponents,
                                           val riskScoreRepo: RiskScoreSettingRepo,
                                           val addRiskToRedis: AddRiskToRedis,
                                           val kafkaService: KafkaService,
                                           val businessImpactRepo: BusinessImpactRepo) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def checkHealth: Action[AnyContent] = Action {
    Ok("Fine")
  }

  def addRiskType: Action[JsValue] = Action.async(parse.json) { request =>
    val riskJson = request.body.as[RiskScoreReq]
 /*   val resp = riskScoreRepo.insertRiskScore(riskJson).map {
      case Left(err) => throw new MatchError(err)
      case Right(data) => data
    }
    val resp1 = addRiskToRedis.publishMerchantRiskType(riskJson.merchantId, riskJson.riskType).map {
      case Left(err) => throw new MatchError(err)
      case Right(data) => data
    }*/
   val resp = for {
      toRedis <- EitherT(riskScoreRepo.insertRiskScore(riskJson))
      //toRdbms <- EitherT(addRiskToRedis.publishMerchantRiskType(riskJson.merchantId, riskJson.riskType))
     toKafka <- EitherT(kafkaService.sendMessage(riskJson.merchantId, riskJson.riskType))
    } yield(toKafka)
    resp.value.map {
      case Left(err) => throw new MatchError(err)
      case Right(_) => Ok.as("application/json")
    }
  }

  def getRiskType(merchantId: String): Action[AnyContent] = Action.async { _ =>
    val resp = riskScoreRepo.fetchRiskScore(merchantId).map {
      case Left(err) => throw new MatchError(err)
      case Right(data) => data
    }
    resp.map (data => Ok(Json.toJson(data)))
  }

  def getBusinessImpactDetail(merchantId: String): Action[AnyContent] = Action.async { _ =>
    val resp = for {
      allow <- EitherT(businessImpactRepo.fetchBusinessDetail(merchantId, "Allow"))
      medium <- EitherT(businessImpactRepo.fetchBusinessDetail(merchantId, "Medium"))
      block <- EitherT(businessImpactRepo.fetchBusinessDetail(merchantId, "Block"))
    } yield(BusinessImpactResp(merchantId, allow.paymentTypeDetail, medium.paymentTypeDetail, block.paymentTypeDetail))
   val result = resp.value.map {
      case Left(err) => throw new MatchError(err)
      case Right(data) => data
    }
    result.map (data => Ok(Json.toJson(data)))
  }
}
