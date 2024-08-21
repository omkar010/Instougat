package com.example.instougat.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Identity::class],
    version = 1,
)
@TypeConverters(DateConverter::class)
abstract class IdentityDatabase: RoomDatabase() {

    abstract fun getIdentityDao(): IdentityDao

}