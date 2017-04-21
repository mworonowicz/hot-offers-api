/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.common

import akka.stream.Supervision
import com.ryanair.hackathon.hotOffers.common.httpClient.HttpException
import org.slf4j.{Logger, LoggerFactory}
import spray.json.DeserializationException

object StreamSupervisionStrategy {
  val log: Logger = LoggerFactory.getLogger(StreamSupervisionStrategy.getClass)

  val decider: Supervision.Decider = {
    case ex: DeserializationException =>
      log.error("Cannot deserialize entity", ex)
      Supervision.Resume
    case ex: HttpException =>
      log.error("Request failed", ex)
      Supervision.Resume
    case ex =>
      log.error("Exception in stream occurred", ex)
      Supervision.Resume
  }
}
