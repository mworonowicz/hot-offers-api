/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.offers.service

import java.time.LocalDate

import akka.http.scaladsl.model.HttpRequest
import cats.data.ReaderT
import com.ryanair.hackathon.hotOffers.common.httpClient.HttpClient
import com.ryanair.hackathon.hotOffers.offers.model.OfferResult
import com.ryanair.hackathon.hotOffers.offers.model.OffersJson._
import com.ryanair.hackathon.hotOffers.users.model.UserDetails
import com.typesafe.config.ConfigFactory
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

object OfferService {

  def getCheapestOffers(userDetails: UserDetails)(implicit executionContext: ExecutionContext):
  ReaderT[Future, HttpClient, OfferResult] = ReaderT((httpClient: HttpClient) => {
    val departureAirport = userDetails.departureAirport
    val arrivalAirports = userDetails.destinationAirports.mkString
    val config = ConfigFactory.load()
    val cheapestOffersUri: String = config.getString("hot-offers.cheapest-offers-uri")
      .replaceAll("departureCode", departureAirport)
      .replaceAll("arrivalCodes", arrivalAirports)
      .replaceAll("dateFrom", LocalDate.now().toString)
      .replaceAll("dateTo", LocalDate.now().plusMonths(6).toString)

    val request = HttpRequest(uri = cheapestOffersUri)
    httpClient.get(request).map(_.parseJson.convertTo[OfferResult])
  })
}
