/*
 * Copyright (c) 2016 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.hotOffers.common.httpClient

class HttpException(message: String = "", cause: Throwable = None.orNull) extends RuntimeException(message, cause)
