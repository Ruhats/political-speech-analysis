package com.fashiondigital.model

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationResponse(
    val mostSpeeches: String?,
    val mostSecurity: String?,
    val leastWordy: String?
)
