package com.example.network.models.response.domain


data class Post(
    val urlType: UrlType,
    val displayUrl: String,
    val url: String,
    val aspectRatio: Float,
    val children: List<Children>?,
    val likes: Long
) {
    enum class UrlType {
        VIDEO, IMAGE, SIDECAR, UNKNOWN
    }

    data class Children(
        val urlType: UrlType,
        val url: String
    )
}
