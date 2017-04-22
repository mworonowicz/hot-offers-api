/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.users.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class UserDetails(userId: String, departureAirport: String, destinationAirports: List[String], budget: Double)

case object UserSaved

object UsersJson extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userDetailsJson: RootJsonFormat[UserDetails] = jsonFormat4(UserDetails)
}
