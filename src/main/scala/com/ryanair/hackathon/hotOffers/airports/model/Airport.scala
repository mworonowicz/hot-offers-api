/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.airports.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Airport(iataCode: String, name: String)

object AirportJson extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val airportJson: RootJsonFormat[Airport] = jsonFormat2(Airport)
}
