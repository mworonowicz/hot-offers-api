/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers

import akka.http.scaladsl.Http

object HotOffersApp extends App with ApplicationContext with Routing {
  Http().bindAndHandle(route, "0.0.0.0", 8080)
}


