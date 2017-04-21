/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.airports.controller

import com.ryanair.hackathon.hotOffers.Controller
import com.ryanair.hackathon.hotOffers.airports.AirportContext
import com.ryanair.hackathon.hotOffers.airports.model.AirportJson._
import com.ryanair.hackathon.hotOffers.airports.service.AirportService

trait AirportController extends Controller {
  airportContext: AirportContext =>

  def route = {
    path("airports") {
      get {
        val airports = AirportService.getAirports().run(airportContext.httpClient)
        complete(airports)
      }
    }

  }
}
