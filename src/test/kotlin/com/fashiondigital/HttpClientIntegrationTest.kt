package com.fashiondigital

import com.fashiondigital.client.HttpClient
import com.fashiondigital.service.CsvParser
import com.fashiondigital.service.SpeechService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import com.fashiondigital.exception.DownloadException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

class HttpClientIntegrationTest {
    private val httpClient = HttpClient()
    private val csvParser = CsvParser()
    private val speechService = SpeechService()

    @Test
    fun `test successful CSV download and processing`() = runBlocking {
        val urls = listOf(
            "https://drive.google.com/uc?export=download&id=1Apwhotvp_KXhILsHc_bJQr2PijT0Sf6V",
            "https://drive.google.com/uc?export=download&id=1CH6ieS0Nid4qh0txOp-G6uwGwFJ3eLSC",
            "https://drive.google.com/uc?export=download&id=1ouTxf8vRDd3gKF7hq92-0lJfBtGF4g3w",
            "https://drive.google.com/uc?export=download&id=1MzCqqaHRyI1T-V1wxuO9IIkKr-1EMFPQ"
        )

        val allSpeeches = urls.flatMap { url ->
            val csvContent = httpClient.downloadCsv(url) // Use your custom HttpClient
            csvParser.parseCsvData(csvContent)
        }

        val statistics = speechService.analyzeSpeeches(allSpeeches)

        assertEquals(null, statistics.mostSpeeches)
        assertEquals("Alexander Abel", statistics.mostSecurity)
        assertEquals("Caesare Collins", statistics.leastWordy)
    }

    @Test
    fun `test CSV download failure`() = runBlocking {
        val invalidUrl = "https://drive.google.com/invalid-url"

        val exception = assertThrows<DownloadException> {
            httpClient.downloadCsv(invalidUrl)
        }

        assertTrue(exception.message?.contains("Failed to download CSV from $invalidUrl") == true)
        assertTrue(exception.message?.contains("404 Not Found") == true)
    }
}
