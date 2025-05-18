package com.micahnyabuto.statussaver.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "status")
data class StatusEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val filePath: String,
    val fileType: String,
    val dateSaved: Long
)
