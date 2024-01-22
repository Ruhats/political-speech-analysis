package com.fashiondigital.service

import com.fashiondigital.model.Speech
import com.fashiondigital.exception.CsvParsingException
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.time.LocalDate
import java.time.format.DateTimeParseException

class CsvParser {
    fun parseCsvData(csvData: String): List<Speech> {
        return try {
            csvReader().readAllWithHeader(csvData).map { row ->
                Speech(
                    speaker = row["Speaker"] ?: throw CsvParsingException("Speaker is missing"),
                    topic = row["Topic"] ?: throw CsvParsingException("Topic is missing"),
                    date = LocalDate.parse(row["Date"]),
                    wordCount = row["Words"]?.toInt() ?: throw CsvParsingException("Words count is missing")
                )
            }
        } catch (e: DateTimeParseException) {
            throw CsvParsingException("Invalid date format in CSV data")
        } catch (e: NumberFormatException) {
            throw CsvParsingException("Invalid number format in CSV data")
        }
    }
}
