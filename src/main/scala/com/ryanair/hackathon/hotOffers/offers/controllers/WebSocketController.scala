package com.ryanair.hackathon.hotOffers.offers.controllers

import akka.http.scaladsl.server.Route
import com.ryanair.hackathon.hotOffers.Controller

trait WebSocketController extends Controller {

 def route: Route =
   get {
     complete("ok")
   }
}
