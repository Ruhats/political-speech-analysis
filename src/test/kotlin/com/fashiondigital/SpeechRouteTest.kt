package com.fashiondigital

import com.fashiondigital.client.HttpClient
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream
import com.fashiondigital.model.EvaluationResponse
import com.fashiondigital.exception.DownloadException
import com.fashiondigital.exception.ExceptionHandlingConfiguration
import com.fashiondigital.plugins.configureMonitoring
import com.fashiondigital.plugins.configureSerialization
import com.fashiondigital.routing.SpeechRoute
import com.fashiondigital.service.CsvParser
import com.fashiondigital.service.SpeechService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.provider.Arguments

class SpeechRouteTest {
    private val httpClientMock = mockk<HttpClient>(relaxed = true)
    private val csvParser = CsvParser()
    private val speechService = SpeechService()

    companion object {
        @JvmStatic
        fun csvDataProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("csv-test-1.csv", EvaluationResponse(null, "Alexander Abel", "Caesare Collins")),
                Arguments.of("csv-test-2.csv", EvaluationResponse("Caesare Collins", null, "Caesare Collins")),
                Arguments.of("csv-test-3.csv", EvaluationResponse(null, null, null)),
                Arguments.of("csv-test-4.csv", EvaluationResponse("Alexander Abel", "Alexander Abel", "Caesare Collins")),
            )
        }

        @JvmStatic
        fun csvFailureDataProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("invalid-column.csv", "Speaker is missing"),
                Arguments.of("invalid-date-format.csv", "Invalid date format in CSV data"),
                Arguments.of("invalid-number-format.csv", "Invalid number format in CSV data")
            )
        }
    }

    @ParameterizedTest
    @MethodSource("csvDataProvider")
    fun `test successful evaluation`(csvFileName: String, expectedResponse: EvaluationResponse) {
        testApplication {
            application {
                configureSerialization()
                SpeechRoute(httpClientMock, csvParser, speechService).registerSpeechRoutes(this)
            }

            val csvContent = readCsvFromFile(csvFileName)
            coEvery { httpClientMock.downloadCsv(any()) } returns csvContent

            val response = client.get("/evaluation?url1=$csvFileName") {
                header(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
            assertEquals(HttpStatusCode.OK, response.status)

            val jsonResponse = response.bodyAsText()
            val actualResponse = Json.decodeFromString<EvaluationResponse>(jsonResponse)

            assertEquals(expectedResponse.mostSpeeches, actualResponse.mostSpeeches)
            assertEquals(expectedResponse.mostSecurity, actualResponse.mostSecurity)
            assertEquals(expectedResponse.leastWordy, actualResponse.leastWordy)
        }
    }

    @ParameterizedTest
    @MethodSource("csvFailureDataProvider")
    fun `test csv parsing failures`(csvFileName: String, expectedErrorMessage: String) {
        testApplication {
            application {
                configureMonitoring()
                ExceptionHandlingConfiguration().configureExceptionHandling(this)
                SpeechRoute(httpClientMock, csvParser, speechService).registerSpeechRoutes(this)
            }

            val csvContent = readCsvFromFile(csvFileName)
            coEvery { httpClientMock.downloadCsv(any()) } returns csvContent

            val response = client.get("/evaluation?url1=$csvFileName")
            assertEquals(HttpStatusCode.BadRequest, response.status)

            val responseContent = response.bodyAsText()
            assertTrue(responseContent.contains(expectedErrorMessage))
        }
    }

    @Test
    fun `test evaluation with DownloadException`() {
        testApplication {
            application {
                configureSerialization()
                ExceptionHandlingConfiguration().configureExceptionHandling(this)
                SpeechRoute(httpClientMock, csvParser, speechService).registerSpeechRoutes(this)
            }
            coEvery { httpClientMock.downloadCsv(any()) } throws DownloadException("Download failed")

            val response = client.get("/evaluation?url1=invalid.csv")

            val responseContent = response.bodyAsText()

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertTrue(responseContent.contains("Download error: Download failed"))
        }
    }

    private fun readCsvFromFile(fileName: String): String {
        val uri = SpeechRouteTest::class.java.classLoader.getResource(fileName)?.toURI()
            ?: throw IllegalArgumentException("File not found: $fileName")
        return Files.readString(Paths.get(uri))
    }
}
