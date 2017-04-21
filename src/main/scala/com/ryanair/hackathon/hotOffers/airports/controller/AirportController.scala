/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.airports.controller

import com.ryanair.hackathon.hotOffers.airports.AirportContext
import com.ryanair.hackathon.hotOffers.airports.service.AirportService

trait AirportController {
  airportContext: AirportContext =>

  def getAirports() = {
    path("airports")

    AirportService.getAirports().run(airportContext.httpClient)
  }
}
