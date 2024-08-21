package com.example.instougat.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface IdentityDao {

    @Upsert
    suspend fun insertIdentity(identity: Identity)

    @Delete
    suspend fun deleteIdentity(identity: Identity)

    @Query("SELECT * FROM identities ORDER BY insertedAt DESC")
    fun getAllIdentities(): LiveData<List<Identity>>
}