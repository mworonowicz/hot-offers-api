/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.common

import java.time.{DayOfWeek, LocalDate, LocalDateTime}

import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

object JsonFormatters {

  implicit object LocalDateJsonFormat extends JsonFormat[LocalDate] {
    def write(date: LocalDate) = JsString(date.toString)

    def read(value: JsValue): LocalDate = value match {
      case JsString(date) => LocalDate.parse(date)
      case x => deserializationError("Expected LocalDate as JsString, but got " + x)
    }

  }

  implicit object LocalDateTimeJsonFormat extends JsonFormat[LocalDateTime] {
    def write(date: LocalDateTime) = JsString(date.toString)

    def read(value: JsValue): LocalDateTime = value match {
      case JsString(date) => LocalDateTime.parse(date)
      case x => deserializationError("Expected LocalDateTime as JsString, but got " + x)
    }

  }

  implicit object DayOfWeekFormat extends JsonFormat[DayOfWeek] {
    override def write(dayOfWeek: DayOfWeek): JsValue = JsString(dayOfWeek.name())

    override def read(value: JsValue): DayOfWeek = value match {
      case JsString(dayOfWeek) => DayOfWeek.valueOf(dayOfWeek)
      case x => deserializationError("Expected DayOfWeek as JsString, but got " + x)
    }
  }

  def deserializationError(msg: String, cause: Throwable = null, fieldNames: List[String] = Nil) = throw DeserializationException(msg, cause, fieldNames)

}
