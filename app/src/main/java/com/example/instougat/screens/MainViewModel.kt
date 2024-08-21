package com.example.instougat.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.KtorClient
import com.example.network.models.response.domain.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val client = KtorClient()
    var res = mutableStateOf(listOf<Post>())
    var loading = mutableStateOf(false)
    var hasNext = mutableStateOf(true)
    var id = "60486190372"
    private var last = ""
    fun resetState(instagramId: String){
        res.value = emptyList()
        id = instagramId
        last = ""
        hasNext.value = true
        getRes()
    }
    fun getRes() {
        if (loading.value || !hasNext.value) return

        loading.value = true


        viewModelScope.launch(Dispatchers.Main) {
            val result = client.getPosts(id = id, after = last.ifEmpty { null })


            result.onSuccess { response ->
                hasNext.value = response.hasNextPage
                last = response.endCursor
                res.value += response.posts
            }.onFailure {
                // Handle error (e.g., show a message to the user)
                Log.d("rrrrrr", it.toString())
            }
            withContext(Dispatchers.Main){
                loading.value = false
            }


        }
    }
}

