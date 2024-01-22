package com.fashiondigital.model

import java.time.LocalDate

data class Speech(
    val speaker: String,
    val topic: String,
    val date: LocalDate,
    val wordCount: Int
)
