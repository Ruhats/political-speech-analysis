package com.fashiondigital.service

import com.fashiondigital.model.Speech
import com.fashiondigital.model.EvaluationResponse

class SpeechService {
    fun analyzeSpeeches(speeches: List<Speech>): EvaluationResponse {
        val mostSpeeches = findUniqueMax(speeches.filter { it.date.year == 2013 }, Speech::speaker)
        val mostSecurity = findUniqueMax(speeches.filter { it.topic == "homeland security" }, Speech::speaker)
        val leastWordy = findUniqueMin(speeches, Speech::speaker)

        return EvaluationResponse(
            mostSpeeches = mostSpeeches,
            mostSecurity = mostSecurity,
            leastWordy = leastWordy
        )
    }

    private fun findUniqueMax(speeches: List<Speech>, selector: (Speech) -> String): String? {
        val counts = speeches.groupingBy(selector).eachCount()
        val maxCount = counts.maxByOrNull { it.value }?.value
        return counts.filterValues { it == maxCount }.keys.singleOrNull()
    }

    private fun findUniqueMin(speeches: List<Speech>, selector: (Speech) -> String): String? {
        val totalWords = speeches.groupingBy(selector).fold(0) { acc, speech -> acc + speech.wordCount }
        val minWords = totalWords.minByOrNull { it.value }?.value
        return totalWords.filterValues { it == minWords }.keys.singleOrNull()
    }
}
