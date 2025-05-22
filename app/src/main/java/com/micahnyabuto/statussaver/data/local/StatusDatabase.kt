package com.micahnyabuto.statussaver.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StatusEntity::class] , version = 1, exportSchema = false)
abstract class StatusDatabase: RoomDatabase() {
    abstract fun statusDao(): StatusDao
}