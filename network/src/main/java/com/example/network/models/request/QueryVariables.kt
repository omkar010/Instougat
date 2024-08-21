package com.example.network.models.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class QueryVariables(
    val id: String,
    val first: Int,
    val after: String? = null
)