/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.common.httpClient

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.{Deflate, Gzip, NoCoding}
import akka.http.scaladsl.model.headers.HttpEncodings
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.{ExecutionContext, Future}


class HttpClient(implicit system: ActorSystem,
                 materializer: ActorMaterializer,
                 val executionContext: ExecutionContext) {

  def get[T](request: HttpRequest): Future[String] = {
    val response: Future[HttpResponse] = Http().singleRequest(request)
    val decodedResponse: Future[HttpResponse] = response.map(decodeResponse)
    val responseEntity: Future[ByteString] = decodedResponse.flatMap {
      case HttpResponse(StatusCodes.OK, _, entity, _) => entity.dataBytes.runFold(ByteString(""))(_ ++ _)
      case resp@HttpResponse(code, _, _, _) =>
        resp.entity.discardBytes()
        Future.failed(throw new HttpException(s"Request to ${request.uri.path.toString()} failed, response code: $code"))
    }
    responseEntity.map(_.utf8String)
  }

  def decodeResponse(response: HttpResponse): HttpResponse = {
    val decoder = response.encoding match {
      case HttpEncodings.gzip =>
        Gzip
      case HttpEncodings.deflate =>
        Deflate
      case HttpEncodings.identity =>
        NoCoding
      case _ =>
        NoCoding
    }

    decoder.decode(response)
  }
}
