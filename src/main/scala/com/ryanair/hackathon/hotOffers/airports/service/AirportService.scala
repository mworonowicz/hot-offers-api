/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.airports.service

import akka.http.scaladsl.model.HttpRequest
import cats.data.ReaderT
import cats.implicits._
import com.ryanair.hackathon.hotOffers.airports.AirportContext
import com.ryanair.hackathon.hotOffers.airports.model.AirportJson._
import com.ryanair.hackathon.hotOffers.airports.model.{Airport, GeoLocation, Route, RouteDetails}
import com.typesafe.config.ConfigFactory
import spray.json._

import scala.concurrent.{ExecutionContext, Future}

object AirportService {

  def getAirports()(implicit executionContext: ExecutionContext): ReaderT[Future, AirportContext, List[Airport]] =
    ReaderT((airportContext: AirportContext) => {
      val config = ConfigFactory.load()
      val airportUri = config.getString("hot-offers.airports-uri")
      val request = HttpRequest(uri = airportUri)
      airportContext.httpClient.get(request).map(res => {
        res.parseJson.convertTo[List[Airport]]
      })
    })

  def getAirport(geoLocation: GeoLocation)(implicit executionContext: ExecutionContext):
  ReaderT[Future, AirportContext, List[Airport]] = ReaderT((airportContext: AirportContext) => {
    val config = ConfigFactory.load()
    val geoLocationUri = config.getString("hot-offers.geolocation-uri")
      .replaceAll("latparam", geoLocation.lat.toString)
      .replaceAll("lonparam", geoLocation.lon.toString)
    val request = HttpRequest(uri = geoLocationUri)
    airportContext.httpClient.get(request).map(res => {
      println(res)
      res.parseJson.convertTo[List[Airport]]
    })
  })

  def getRoutesFromAirport(iataCode: String)(implicit executionContext: ExecutionContext):
  ReaderT[Future, AirportContext, List[RouteDetails]] = ReaderT((airportContext: AirportContext) => {
    val config = ConfigFactory.load()
    val routesUri = config.getString("hot-offers.routes-from-airport-uri").replaceAll("iataCode", iataCode)
    val request = HttpRequest(uri = routesUri)
    val readerAirports: ReaderT[Future, AirportContext, List[Airport]] = getAirports()
    val futureRoutes: Future[List[Route]] = airportContext.httpClient.get(request)
      .map(_.parseJson.convertTo[List[Route]])
    val readerRoutes = ReaderT[Future, AirportContext, List[Route]](_ => futureRoutes)

    (for {
      routes <- readerRoutes
      airports <- readerAirports
    } yield routes
      .map(route => airports.find(_.iataCode == route.airportTo)
        .map(airport => RouteDetails(route.airportTo, airport.name))
        .getOrElse(RouteDetails(route.airportTo, "N/A")))
      )
      .run(airportContext)
  })

}
