/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.common

import akka.http.scaladsl.unmarshalling.Unmarshaller

object ListUnmarshaller {
  implicit val intsFromStringUnmarshaller: Unmarshaller[String, List[Int]] =
    listUnmarshaller(strings => toList(strings, _.toInt), "List[Int]")

  private def toList[T](strings: String, f: String => T): List[T] = {
    strings.split(",").toList
      .map(s => f(s))
  }

  private def listUnmarshaller[T](f: String ⇒ T, target: String): Unmarshaller[String, T] =
    Unmarshaller.strict[String, T] { string ⇒
      try f(string)
      catch numberFormatError(string, target)
    }

  private def numberFormatError(value: String, target: String): PartialFunction[Throwable, Nothing] = {
    case e: NumberFormatException ⇒
      throw if (value.isEmpty) Unmarshaller.NoContentException else new IllegalArgumentException(s"'$value' is not a valid $target value", e)
  }
}
