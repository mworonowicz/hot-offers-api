package com.ryanair.hackathon.hotOffers.offers

import akka.stream.ActorMaterializer
import com.ryanair.hackathon.hotOffers.common.httpClient.HttpClient
import com.ryanair.hackathon.hotOffers.offers.model.OffersNotificationConfig

import scala.concurrent.ExecutionContext


trait OffersContext {

  implicit def materializer: ActorMaterializer

  implicit def executionContext: ExecutionContext

  def httpClient: HttpClient

  def notificationConfig: OffersNotificationConfig

}
