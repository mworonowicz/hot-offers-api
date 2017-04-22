/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.users.service

import java.util.concurrent.ConcurrentHashMap

import com.ryanair.hackathon.hotOffers.offers.model.{Offer, OfferResult}
import com.ryanair.hackathon.hotOffers.users.model.{UserDetails, UserSaved}

import scala.collection.JavaConverters._
import scala.concurrent.Future

object UserDetailsService {
  private val db = new ConcurrentHashMap[String, UserDetails]()
  private val offersDb = new ConcurrentHashMap[String, Map[String, Float]]()


  def saveDetails(userDetails: UserDetails): Future[UserSaved.type] = {
    db.put(userDetails.userId, userDetails)
    offersDb.remove(userDetails.userId)
    Future.successful(UserSaved)
  }

  def getAll: List[UserDetails] = {
    db.elements().asScala.toList
  }

  def saveOffersForUser(user: UserDetails, offers: List[Offer]): Future[OfferResult] = {
    val result = offersDb.asScala.get(user.userId) match {
      case Some(offersMap) =>
        val filtered = for {
          offer <- offers
          if offer.outbound.price.value < offersMap.getOrElse(offer.outbound.arrivalAirport.iataCode, Float.MaxValue)
          if offer.outbound.price.value <= user.budget
        } yield offer

        filtered.take(1).map(firstOffer => {
          offersDb.put(user.userId, offersMap.updated(firstOffer.outbound.arrivalAirport.iataCode, firstOffer.outbound.price.value))
          firstOffer
        })
      case None =>
        val offersToStore = offers.take(1)
        offersDb.put(user.userId, offersToMap(offersToStore))
        offersToStore

    }
    Future.successful(OfferResult(result.length, result))
  }

  private def offersToMap(offers: List[Offer]) = {
    Map(offers.map(o => (o.outbound.arrivalAirport.iataCode, o.outbound.price.value)): _*)
  }
}
