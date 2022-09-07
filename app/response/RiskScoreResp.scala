package response

import play.api.libs.json.{Format, Json}

case class RiskScoreResp(riskType: String)

object RiskScoreResp {
  implicit val format: Format[RiskScoreResp] = Json.format
}