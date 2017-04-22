package com.ryanair.hackathon.hotOffers

import akka.http.scaladsl.server.{Directives, Route}
import com.ryanair.hackathon.hotOffers.airports.controller.AirportController
import com.ryanair.hackathon.hotOffers.offers.controller.WebSocketController
import com.ryanair.hackathon.hotOffers.users.controller.UserDetailsController
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

trait Routing extends AirportController with UserDetailsController with WebSocketController {
  application: ApplicationContext =>

  override def route: Route = cors() {
    pathPrefix("hot-offers") {
      super[WebSocketController].route ~ super[AirportController].route ~ super[UserDetailsController].route
    }
  }
}

trait Controller extends Directives {
  def route: Route
}