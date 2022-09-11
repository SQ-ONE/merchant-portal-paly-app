package com.squareoneinsights.merchantportal.marchantportalappstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.squareoneinsights.merchantportal.marchantportalappstream.api.MarchantportalappStreamService
import com.squareoneinsights.merchantportal.marchantportalapp.api.MarchantportalappService

import scala.concurrent.Future

/**
  * Implementation of the MarchantportalappStreamService.
  */
class MarchantportalappStreamServiceImpl(marchantportalappService: MarchantportalappService) extends MarchantportalappStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(marchantportalappService.hello(_).invoke()))
  }
}
