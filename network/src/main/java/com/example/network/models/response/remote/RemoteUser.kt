package com.example.network.models.response.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class RemoteUser(
    val data: Data
){
    @Serializable
    data class Data(
       val user: User
    ){
        @Serializable
        data class User(
            val id: String,
            val full_name: String,
            val username: String,
            val profile_pic_url: String,
            val profile_pic_url_hd: String
        )
    }
}