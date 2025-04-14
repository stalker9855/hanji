package com.dev.hanji.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.hanji.data.model.AchievementEntity
import kotlinx.coroutines.flow.Flow

data class KanjiWithAttemptStatus(
    val character: String,
    val isAttempted: Boolean
)


@Dao
interface AchievementDao {

    @Query("SELECT k.character," +
            " CASE WHEN ka.character IS NOT NULL" +
            " THEN 1 ELSE 0 END AS isAttempted " +
            "FROM kanji k " +
            "LEFT JOIN kanji_attempts ka on k.character = ka.character")
    fun getKanjiWithAttemptStatus(): PagingSource<Int, KanjiWithAttemptStatus>

    @Delete
    suspend fun delete(achievement: AchievementEntity)
}