package com.ryanair.hackathon.hotOffers

import akka.http.scaladsl.server.{Directives, Route}
import com.ryanair.hackathon.hotOffers.airports.controller.AirportController
import com.ryanair.hackathon.hotOffers.offers.controllers.WebSocketController

trait Routing extends AirportController with WebSocketController {
  application: ApplicationContext =>

  override def route: Route = pathPrefix("hot-offers") {
    super[WebSocketController].route ~ super[AirportController].route
  }
}


trait Controller extends Directives {
  def route: Route
}