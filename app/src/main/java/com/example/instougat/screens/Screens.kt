package com.example.instougat.screens

import kotlinx.serialization.Serializable

@Serializable
object AccountsScreenObject

@Serializable
data class PostsScreenObject(
    val id: String
)

@Serializable
data class VideoScreenObject(
    val url: String
)