/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.common

import java.time.temporal.ChronoUnit
import java.time.{DayOfWeek, LocalDate}

object DateGenerator {

  def generateAllowedDates(startDate: LocalDate, endDate: LocalDate, daysOfWeek: List[DayOfWeek]): List[LocalDate] = {
    val days = startDate.until(endDate, ChronoUnit.DAYS)
    List.range(0, days + 1)
      .map(startDate.plusDays)
      .filter(isDateAllowed(daysOfWeek, _))
  }

  private def isDateAllowed(daysOfWeek: List[DayOfWeek], date: LocalDate): Boolean = {
    daysOfWeek.contains(date.getDayOfWeek)
  }
}
