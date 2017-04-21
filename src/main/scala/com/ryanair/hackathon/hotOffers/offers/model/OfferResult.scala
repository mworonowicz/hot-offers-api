/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.offers.model

import java.time.LocalDateTime

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.ryanair.hackathon.hotOffers.airports.model.Airport
import com.ryanair.hackathon.hotOffers.airports.model.AirportJson._
import com.ryanair.hackathon.hotOffers.common.JsonFormatters._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class OfferResult(total: Int, fares: List[Offer])

case class Offer(outbound: Flight)

case class Flight(departureAirport: Airport, arrivalAirport: Airport, departureDate: LocalDateTime, arrivalDate: LocalDateTime,
                  price: Price)

case class Price(value: Float, currencyCode: String)

object OffersJson extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val priceJson: RootJsonFormat[Price] = jsonFormat2(Price)
  implicit val flightJson: RootJsonFormat[Flight] = jsonFormat5(Flight)
  implicit val offerJson: RootJsonFormat[Offer] = jsonFormat1(Offer)
  implicit val offerResultJson: RootJsonFormat[OfferResult] = jsonFormat2(OfferResult)
}
