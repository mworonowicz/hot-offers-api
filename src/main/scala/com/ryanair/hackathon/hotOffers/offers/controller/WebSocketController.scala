package com.ryanair.hackathon.hotOffers.offers.controller

import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ActorAttributes, Supervision}
import com.ryanair.hackathon.hotOffers.offers.model.OfferResult
import com.ryanair.hackathon.hotOffers.offers.service.OfferService
import com.ryanair.hackathon.hotOffers.users.service.UserDetailsService
import com.ryanair.hackathon.hotOffers.{ApplicationContext, Controller}
import spray.json._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

trait WebSocketController extends Controller {
  context: ApplicationContext =>
  def getOffers =
    Flow[Message].map {
      case tm: TextMessage =>
        tm.textStream.flatMapConcat(userId => {
          println(userId)
          Source.tick(0 seconds, 10 seconds, ())
            .mapAsync(1)(_ =>
              UserDetailsService.getAll
                .find(_.userId == userId)
                .fold[Future[OfferResult]](Future.failed(new RuntimeException("User does not exist"))) {
                user =>
                  OfferService.getCheapestOffers(user).run(context
                    .httpClient)
              })
            .withAttributes(ActorAttributes.supervisionStrategy(Supervision.stoppingDecider))
        }.recover {
          case ex => TextMessage("user not found")
        }).map(o => o.toJson.toString)
      case bm: BinaryMessage =>
        // ignore binary messages but drain content to avoid the stream being clogged
        bm.dataStream.runWith(Sink.ignore)
        Source.empty
    }.flatMapConcat(identity).map(TextMessage(_))

  def route: Route =
    path("offers") {
      handleWebSocketMessages(getOffers)
    }
}
