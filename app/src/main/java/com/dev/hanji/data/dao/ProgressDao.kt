package com.dev.hanji.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.dev.hanji.data.model.AchievementEntity
import com.dev.hanji.data.state.InitialProgressState
import kotlinx.coroutines.flow.Flow

data class KanjiWithAttemptStatus(
    val character: String,
    val isAttempted: Boolean
)


@Dao
interface ProgressDao {

    @Query("SELECT k.character," +
            " CASE WHEN ka.character IS NOT NULL" +
            " THEN 1 ELSE 0 END AS isAttempted " +
            "FROM kanji k " +
            "LEFT JOIN kanji_attempts ka on k.character = ka.character")
    fun getKanjiWithAttemptStatus(): PagingSource<Int, KanjiWithAttemptStatus>



    @Query("""
    SELECT 
        COUNT(k.character) AS total, 
        COUNT(DISTINCT ka.character) AS attempted
    FROM kanji k 
    LEFT JOIN kanji_attempts ka ON k.character = ka.character
""")
    fun getAttempted(): Flow<InitialProgressState>


    @Delete
    suspend fun delete(achievement: AchievementEntity)
}