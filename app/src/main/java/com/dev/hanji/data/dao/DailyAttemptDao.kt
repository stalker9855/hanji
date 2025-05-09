package com.dev.hanji.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.hanji.data.model.DailyAttempt
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyAttemptDao {

    @Query("SELECT * FROM daily_attempts ORDER BY date ASC LIMIT 10")
    fun getDailyAttempts(): Flow<List<DailyAttempt>>


    @Query("SELECT * FROM daily_attempts where date = :date")
    fun getByDate(date: String): DailyAttempt?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: DailyAttempt)

    @Update
    suspend fun update(entity: DailyAttempt)
}