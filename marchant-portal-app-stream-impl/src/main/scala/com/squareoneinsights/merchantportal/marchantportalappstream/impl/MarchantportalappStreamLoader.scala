package com.squareoneinsights.merchantportal.marchantportalappstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.squareoneinsights.merchantportal.marchantportalappstream.api.MarchantportalappStreamService
import com.squareoneinsights.merchantportal.marchantportalapp.api.MarchantportalappService
import com.softwaremill.macwire._

class MarchantportalappStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new MarchantportalappStreamApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new MarchantportalappStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[MarchantportalappStreamService])
}

abstract class MarchantportalappStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[MarchantportalappStreamService](wire[MarchantportalappStreamServiceImpl])

  // Bind the MarchantportalappService client
  lazy val marchantportalappService: MarchantportalappService = serviceClient.implement[MarchantportalappService]
}
