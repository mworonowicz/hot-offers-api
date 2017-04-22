/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.airports.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Airport(iataCode: String, name: String)

case class Route(airportFrom: String, airportTo: String)

case class RouteDetails(iataCode: String, name: String)

case class GeoLocation(lat: Double, lon: Double)

object AirportJson extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val airportJson: RootJsonFormat[Airport] = jsonFormat2(Airport)
  implicit val routeJson: RootJsonFormat[Route] = jsonFormat2(Route)
  implicit val routeDetailsJson: RootJsonFormat[RouteDetails] = jsonFormat2(RouteDetails)
}
