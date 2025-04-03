package com.dev.hanji.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dev.hanji.data.model.KanjiAttemptEntity

@Dao
interface KanjiAttemptDao {

    @Upsert
    suspend fun insert(kanjiAttempt: KanjiAttemptEntity)

    @Query("SELECT * FROM kanji_attempts")
    suspend fun getAllAttemptsKanji(): List<KanjiAttemptEntity>

}