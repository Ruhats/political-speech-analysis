package com.fashiondigital.exception

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.plugins.statuspages.*

class ExceptionHandlingConfiguration {
    fun configureExceptionHandling(application: Application) {
        with(application){
            install(StatusPages) {
                exception<DownloadException> { call, cause ->
                    call.respond(HttpStatusCode.BadRequest, "Download error: ${cause.message}")
                    call.application.log.error("DownloadException: ${cause.localizedMessage}", cause)
                }

                exception<CsvParsingException> { call, cause ->
                    call.respond(HttpStatusCode.BadRequest, "CSV parsing error: ${cause.message}")
                    call.application.log.error("CsvParsingException: ${cause.localizedMessage}", cause)
                }

                exception<Throwable> { call, cause ->
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error: ${cause.localizedMessage}")
                    call.application.log.error("Unhandled exception: ${cause.localizedMessage}", cause)
                }
            }
        }
    }
}


class DownloadException(message: String) : RuntimeException(message)
class CsvParsingException(message: String) : RuntimeException(message)
