/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.common.httpClient

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.{ExecutionContext, Future}


class HttpClient(implicit system: ActorSystem,
                 materializer: ActorMaterializer,
                 val executionContext: ExecutionContext) {

  def get[T](request: HttpRequest): Future[String] = {
    val response: Future[HttpResponse] = Http().singleRequest(request)
    val responseEntity = response.flatMap {
      case HttpResponse(StatusCodes.OK, _, entity, _) => entity.dataBytes.runWith(Sink.head)
      case resp@HttpResponse(code, _, _, _) =>
        resp.entity.discardBytes()
        Future.failed(throw new HttpException(s"Request to ${request.uri.path.toString()} failed, response code: $code"))
    }
    responseEntity.map(_.utf8String)
  }
}
