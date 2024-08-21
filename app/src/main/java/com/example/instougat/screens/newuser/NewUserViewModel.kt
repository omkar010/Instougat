package com.example.instougat.screens.newuser

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instougat.MainApplication
import com.example.instougat.room.Identity
import com.example.network.KtorClient
import kotlinx.coroutines.launch

class NewUserViewModel: ViewModel() {
    private val ktorClient = KtorClient()
    private val identityDao = MainApplication.identityDatabase.getIdentityDao()

    val res = mutableStateOf("")

    private fun addAccount(identity: Identity){
        viewModelScope.launch {
            identityDao.insertIdentity(identity)
        }
    }

    fun getUser(username: String){
        viewModelScope.launch {
            ktorClient.getUser(username).onSuccess { item->
                val identity = Identity(
                    instagramId = item.data.user.id,
                    username = item.data.user.username,
                    name = item.data.user.full_name,
                    profilePicUrl = item.data.user.profile_pic_url
                )
                addAccount(identity)
                res.value = identity.toString()
            }.onFailure {
                res.value = "something went wrong"
            }
        }
    }
}