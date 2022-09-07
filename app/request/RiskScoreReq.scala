package request

import play.api.libs.json.{Format, Json}

case class RiskScoreReq(merchantId: String,
                        riskType: String)

object RiskScoreReq {
  implicit val format: Format[RiskScoreReq] = Json.format
}