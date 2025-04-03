package com.dev.hanji.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.hanji.data.model.AchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(achievement: AchievementEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(achievement: AchievementEntity)

    @Query("SELECT * FROM current_achievements")
    fun get(): Flow<AchievementEntity>

    @Delete
    suspend fun delete(achievement: AchievementEntity)
}