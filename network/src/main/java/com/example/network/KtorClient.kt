package com.example.network

import android.util.Log
import com.example.network.models.request.QueryVariables
import com.example.network.models.response.domain.Post
import com.example.network.models.response.remote.PostsPage
import com.example.network.models.response.remote.RemotePosts
import com.example.network.models.response.remote.RemoteUser
import com.example.network.models.response.remote.toDomainPosts
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorClient {

    // Define the device information
    private val defaultUserAgent = "Instagram 26.0.0.10.86 Android (24/7.0; 640dpi; 1440x2560; HUAWEI; LON-L29; HWLON; hi3660; en_US)"

    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
                explicitNulls = false
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.INFO
        }
    }

    suspend fun getPosts(id: String, after: String? = null): ApiOperation<PostsPage> {
        val variables = QueryVariables(id = id, first = 12, after = after)
        val variablesJson = Json.encodeToString(variables)
        val url = "https://www.instagram.com/graphql/query/?doc_id=7950326061742207&variables=$variablesJson"

        return safeApiCall {
            client.get(url).body<RemotePosts>().toDomainPosts()
        }
    }

    suspend fun getUser(username: String): ApiOperation<RemoteUser> {
        val url = "https://www.instagram.com/api/v1/users/web_profile_info/?username=$username"

        return safeApiCall {
            client.get(url) {
                headers {
                    append(HttpHeaders.UserAgent, defaultUserAgent)
                }
            }.body<RemoteUser>()
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(data = apiCall())
        } catch (e: Exception) {
            ApiOperation.Failure(exception = e)
        }
    }
}
sealed interface ApiOperation<T> {
    data class Success<T>(val data: T) : ApiOperation<T>
    data class Failure<T>(val exception: Exception) : ApiOperation<T>

    fun onSuccess(block: (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(exception)
        return this
    }
}