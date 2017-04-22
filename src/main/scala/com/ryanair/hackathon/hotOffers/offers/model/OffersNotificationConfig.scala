package com.ryanair.hackathon.hotOffers.offers.model

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.FiniteDuration


object OffersNotificationConfig {

  private val intervalInSeconds = ConfigFactory.load.getDuration("notification.interval", TimeUnit.SECONDS)

  def fromFile() = OffersNotificationConfig(FiniteDuration(intervalInSeconds, TimeUnit.SECONDS))

}

case class OffersNotificationConfig(interval: FiniteDuration)


