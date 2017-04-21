/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers

import akka.http.scaladsl.Http
import com.ryanair.hackathon.hotOffers.offers.service.OfferService
import com.ryanair.hackathon.hotOffers.users.model.UserDetails

object HotOffersApp extends App with ApplicationContext with Routing {
  val userDetails = UserDetails("1234", "WRO", List("LPL"))
  OfferService.getCheapestOffers(userDetails).run(httpClient).onComplete(println)
  Http().bindAndHandle(route, "0.0.0.0", 8080)
}
