/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.airports.service

import akka.http.scaladsl.model.HttpRequest
import cats.data.Reader
import com.ryanair.hackathon.hotOffers.airports.model.Airport
import com.ryanair.hackathon.hotOffers.airports.model.AirportJson._
import com.ryanair.hackathon.hotOffers.common.httpClient.HttpClient
import com.typesafe.config.ConfigFactory
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

object AirportService {
  def getAirports()(implicit executionContext: ExecutionContext): Reader[HttpClient, Future[List[Airport]]] =
    Reader((httpClient: HttpClient) => {
      val config = ConfigFactory.load()
      val airportUri = config.getString("hot-offers.airports-uri")
      val request = HttpRequest(uri = airportUri)
      httpClient.get(request).map(res => {
        res.parseJson.convertTo[List[Airport]]
      })
    })
}
