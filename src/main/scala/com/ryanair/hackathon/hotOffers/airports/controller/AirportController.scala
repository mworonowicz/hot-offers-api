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
    pathPrefix("airports") {
      pathEnd {
        get {
          val airports = AirportService.getAirports().run(airportContext)
          complete(airports)
        }
      } ~
        path(Segment) { iataCode =>
          val routes = AirportService.getRoutesFromAirport(iataCode).run(airportContext)
          complete(routes)
        }
    }
  }
}
