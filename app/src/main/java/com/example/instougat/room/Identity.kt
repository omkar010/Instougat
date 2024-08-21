package com.example.instougat.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "identities")
data class Identity(
    @PrimaryKey(autoGenerate = false)
    val instagramId: String,
    val name: String,
    val username: String,
    val profilePicUrl: String,

    @TypeConverters(DateConverter::class)
    val insertedAt: Date = Date()
)