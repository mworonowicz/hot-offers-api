/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.users.controller

import com.ryanair.hackathon.hotOffers.Controller
import com.ryanair.hackathon.hotOffers.users.model.UserDetails
import com.ryanair.hackathon.hotOffers.users.model.UsersJson._
import com.ryanair.hackathon.hotOffers.users.service.UserDetailsService

trait UserDetailsController extends Controller {

  def route =
    pathPrefix("user-details") {
      pathEnd {
        post {
          entity(as[UserDetails]) { userDetails =>
            val saved = UserDetailsService.saveDetails(userDetails)
            onComplete(saved) { _ =>
              complete(s"User ${userDetails.userId} saved")
            }
          }
        } ~
          get {
            complete(UserDetailsService.getAll)
          }
      }
    }
}
