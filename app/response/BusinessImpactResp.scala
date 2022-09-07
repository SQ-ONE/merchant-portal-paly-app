package response

import model.PaymentTypeDetail
import play.api.libs.json.{Format, Json}

case class BusinessImpactResp(merchantId: String, blockMoreFraud: PaymentTypeDetail, medium: PaymentTypeDetail, allowMorePayments: PaymentTypeDetail)

object BusinessImpactResp {

  implicit val format: Format[BusinessImpactResp] = Json.format
}