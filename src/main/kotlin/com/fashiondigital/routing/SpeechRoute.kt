package com.fashiondigital.routing

import com.fashiondigital.client.HttpClient
import com.fashiondigital.service.CsvParser
import com.fashiondigital.service.SpeechService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SpeechRoute(
    private val httpClient: HttpClient,
    private val csvParser: CsvParser,
    private val speechService: SpeechService
) {
    fun registerSpeechRoutes(application: Application) {
        application.routing {
            get("/evaluation") {
                coroutineScope {
                    val urlParameters = call.request.queryParameters
                    val csvUrls = urlParameters.entries()
                        .filter { it.key.startsWith("url") }
                        .flatMap { it.value }

                    val csvContents = csvUrls
                        .map { url -> async { httpClient.downloadCsv(url) } }
                        .awaitAll()

                    val speeches = csvContents.flatMap { csvContent ->
                        csvParser.parseCsvData(csvContent)
                    }

                    val statistics = speechService.analyzeSpeeches(speeches)
                    call.respond(statistics)
                }
            }
        }
    }
}
