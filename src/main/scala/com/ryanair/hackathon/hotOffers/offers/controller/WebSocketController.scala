package com.ryanair.hackathon.hotOffers.offers.controller

import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ActorAttributes, Supervision}
import com.ryanair.hackathon.hotOffers.Controller
import com.ryanair.hackathon.hotOffers.offers.OffersContext
import com.ryanair.hackathon.hotOffers.offers.model.OffersJson._
import com.ryanair.hackathon.hotOffers.offers.service.OfferService
import com.typesafe.scalalogging.LazyLogging
import spray.json._

import scala.concurrent.duration._
import scala.language.postfixOps

trait WebSocketController extends Controller with LazyLogging {
  context: OffersContext =>


  def route: Route =
    path("offers") {
      handleWebSocketMessages(createOffersNotificationFlow)
    }

  private def createOffersNotificationFlow =
    Flow[Message].map {
      case tm: TextMessage =>
        tm.textStream.flatMapConcat(userIdWithSession => {
          logger.info(s" User $userIdWithSession has been connected")
          val userInfo = userIdWithSession.split(":")
          val (userId, session) = (userInfo(0), userInfo(1))
          Source.tick(0 seconds, context.notificationConfig.interval, ())
            .mapAsync(1)(_ => OfferService.getOffersForUser(userId, session).run(context))
            .withAttributes(ActorAttributes.supervisionStrategy(Supervision.restartingDecider))
        })
          .map(o => o.toJson.toString)

      case bm: BinaryMessage =>
        // ignore binary messages but drain content to avoid the stream being clogged
        bm.dataStream.runWith(Sink.ignore)
        Source.empty
    }.flatMapConcat(identity).map(TextMessage(_))
}
