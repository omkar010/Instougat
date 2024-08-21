package com.example.instougat.screens.accounts

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instougat.MainApplication
import com.example.instougat.room.Identity
import kotlinx.coroutines.launch

class AccountsViewModel: ViewModel() {



    private val identityDao = MainApplication.identityDatabase.getIdentityDao()

    val accountList: LiveData<List<Identity>> = identityDao.getAllIdentities()

    fun addAccount(identity: Identity){
        viewModelScope.launch {
            identityDao.insertIdentity(identity)
        }
    }
    fun deleteAccount(identity: Identity){
        viewModelScope.launch {
            identityDao.deleteIdentity(identity)
        }
    }
}