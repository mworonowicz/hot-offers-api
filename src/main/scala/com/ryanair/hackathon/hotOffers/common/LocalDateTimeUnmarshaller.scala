/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.common

import java.time.LocalDateTime
import java.time.format.DateTimeParseException

import akka.http.scaladsl.unmarshalling.Unmarshaller

object LocalDateTimeUnmarshaller {
  implicit val localDateTimeFromStringUnmarshaller: Unmarshaller[String, LocalDateTime] =
    localDateUnmarshaller(date => toLocalDateTime(date), "LocalDate")

  private def toLocalDateTime(date: String): LocalDateTime = {
    LocalDateTime.parse(date)
  }

  private def localDateUnmarshaller[T](f: String ⇒ LocalDateTime, target: String): Unmarshaller[String, LocalDateTime] =
    Unmarshaller.strict[String, LocalDateTime] { string ⇒
      try f(string)
      catch localDateFormatError(string, target)
    }

  private def localDateFormatError(value: String, target: String): PartialFunction[Throwable, Nothing] = {
    case e: DateTimeParseException ⇒
      throw if (value.isEmpty) Unmarshaller.NoContentException else new IllegalArgumentException(s"'$value' is not a valid $target value", e)
  }
}