package com.dev.hanji.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dev.hanji.data.model.KanjiAttemptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KanjiAttemptDao {

    @Upsert
    suspend fun insert(kanjiAttempt: KanjiAttemptEntity)

    @Query("SELECT * FROM kanji_attempts")
    fun getAllAttemptsKanji(): Flow<List<KanjiAttemptEntity>>

}