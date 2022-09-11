package com.squareoneinsights.merchantportal.marchantportalappstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * The marchant-portal-app stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the MarchantportalappStream service.
  */
trait MarchantportalappStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor: Descriptor = {
    import Service._

    named("marchant-portal-app-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

