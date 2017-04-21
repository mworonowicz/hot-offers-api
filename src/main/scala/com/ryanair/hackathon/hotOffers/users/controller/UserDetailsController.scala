/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.users.controller

import com.ryanair.hackathon.hotOffers.Controller
import com.ryanair.hackathon.hotOffers.users.model.UserDetails
import com.ryanair.hackathon.hotOffers.users.model.UsersJson._

import scala.concurrent.Future

trait UserDetailsController extends Controller {
  def route =
    pathPrefix("user-details") {
      pathEnd {
        post {
          entity(as[UserDetails]) { userDetails =>
            println(userDetails)
            val saved = Future.successful("saved")
            onComplete(saved) { done =>
              complete("userDetails saved")
            }
          }
        }
      }
    }
}
