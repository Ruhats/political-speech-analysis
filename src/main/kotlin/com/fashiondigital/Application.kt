package com.fashiondigital

import com.fashiondigital.plugins.*
import com.fashiondigital.client.HttpClient
import com.fashiondigital.exception.ExceptionHandlingConfiguration
import com.fashiondigital.routing.SpeechRoute
import com.fashiondigital.service.CsvParser
import com.fashiondigital.service.SpeechService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()

    ExceptionHandlingConfiguration().configureExceptionHandling(this)

    val httpClient = HttpClient()
    val csvParser = CsvParser()
    val speechService = SpeechService()

    SpeechRoute(httpClient, csvParser, speechService).registerSpeechRoutes(this)
}

