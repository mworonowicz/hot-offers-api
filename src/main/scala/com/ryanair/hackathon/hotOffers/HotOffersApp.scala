/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers

import com.ryanair.hackathon.hotOffers.airports.service.AirportService

object HotOffersApp extends App with ApplicationContext {
  val airports = AirportService.getAirports().run(httpClient)
  airports.onComplete(println)
}
