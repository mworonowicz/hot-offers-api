/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.users.service

import java.util.concurrent.ConcurrentHashMap

import com.ryanair.hackathon.hotOffers.users.model.{UserDetails, UserSaved}

import scala.collection.JavaConverters._
import scala.concurrent.Future

object UserDetailsService {
  private val db = new ConcurrentHashMap[String, UserDetails]()

  def saveDetails(userDetails: UserDetails): Future[UserSaved.type] = {
    db.put(userDetails.userId, userDetails)
    Future.successful(UserSaved)
  }

  def getAll: List[UserDetails] = {
    db.elements().asScala.toList
  }

}
