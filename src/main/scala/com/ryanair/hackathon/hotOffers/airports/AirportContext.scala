/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.airports

import com.ryanair.hackathon.hotOffers.common.httpClient.HttpClient

import scala.concurrent.ExecutionContext

trait AirportContext {
  implicit val executionContext: ExecutionContext

  def httpClient: HttpClient
}
