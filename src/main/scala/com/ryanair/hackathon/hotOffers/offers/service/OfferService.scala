/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.offers.service

import java.time.LocalDate

import akka.http.scaladsl.model.HttpRequest
import cats.data.{EitherT, ReaderT}
import cats.implicits._
import com.ryanair.hackathon.hotOffers.common.httpClient.HttpClient
import com.ryanair.hackathon.hotOffers.offers.OffersContext
import com.ryanair.hackathon.hotOffers.offers.model.OfferResult
import com.ryanair.hackathon.hotOffers.offers.model.OffersJson._
import com.ryanair.hackathon.hotOffers.users.model.UserDetails
import com.ryanair.hackathon.hotOffers.users.service.UserDetailsService
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

object OfferService extends LazyLogging {

  def getCheapestOffers(userDetails: UserDetails)(implicit executionContext: ExecutionContext):
  ReaderT[Future, HttpClient, OfferResult] = ReaderT((httpClient: HttpClient) => {
    val departureAirport = userDetails.departureAirport
    val arrivalAirports = userDetails.destinationAirports.mkString(",")
    val config = ConfigFactory.load()
    val cheapestOffersUri: String = config.getString("hot-offers.cheapest-offers-uri")
      .replaceAll("departureCode", departureAirport)
      .replaceAll("arrivalCodes", arrivalAirports)
      .replaceAll("dateFrom", LocalDate.now().toString)
      .replaceAll("dateTo", LocalDate.now().plusMonths(6).toString)

    val request = HttpRequest(uri = cheapestOffersUri)
    httpClient.get(request).map(res => {
      logger.info(s"Retrieving fares for user ${userDetails.userId}: $res")
      res.parseJson.convertTo[OfferResult]
    })
  })

  def getOffersForUser(userId: String): ReaderT[Future, OffersContext, OfferResult] = ReaderT {
    context =>
      import context._
      val offers = for {
        userDetails <- EitherT.fromEither[Future](UserDetailsService.getAll.find(_.userId == userId).toRight("User not found"))
        offers <- EitherT.liftT[Future, String, OfferResult](getCheapestOffers(userDetails).run(httpClient))
        savedOffers <- EitherT.liftT[Future, String, OfferResult](UserDetailsService.saveOffersForUser(userDetails, offers.fares))
      } yield savedOffers
      offers.valueOr(msg => throw new RuntimeException(msg))
  }

}
