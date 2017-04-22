/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.ryanair.hackathon.hotOffers.airports.AirportContext
import com.ryanair.hackathon.hotOffers.common.StreamSupervisionStrategy
import com.ryanair.hackathon.hotOffers.common.httpClient.HttpClient
import com.typesafe.sslconfig.akka.AkkaSSLConfig

import scala.concurrent.ExecutionContext

trait ApplicationContext extends AirportContext {
  implicit val system: ActorSystem = ActorSystem("hot-offers-system")
  implicit val materializer: ActorMaterializer =
    ActorMaterializer(ActorMaterializerSettings(system).withSupervisionStrategy(StreamSupervisionStrategy.decider))
  val sslConfig = AkkaSSLConfig()
  implicit val executionContext: ExecutionContext = materializer.executionContext
  val httpClient = new HttpClient()

}
