package com.micahnyabuto.statussaver.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatus(status: StatusEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(statusList: List<StatusEntity>)

    @Query("SELECT * FROM status ORDER BY dateSaved DESC")
     fun getAllStatuses(): Flow<List<StatusEntity>>

    @Query("DELETE FROM status WHERE id = :statusId")
    suspend fun deleteStatusById(statusId: Int)

    @Query("DELETE FROM status")
    suspend fun deleteAllStatuses()
}