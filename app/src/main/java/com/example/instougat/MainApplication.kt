package com.example.instougat

import android.app.Application
import androidx.room.Room
import com.example.instougat.room.IdentityDatabase

class MainApplication: Application() {

    companion object {
        lateinit var identityDatabase: IdentityDatabase
    }

    override fun onCreate() {
        super.onCreate()
        identityDatabase = Room.databaseBuilder(
            applicationContext,
            IdentityDatabase::class.java,
            "identities"
        ).build()
    }
}