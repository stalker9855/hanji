package com.dev.hanji.kanji

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KanjiAttemptDao {

    @Upsert
    suspend fun insert(kanjiAttempt: KanjiAttemptEntity)

    @Query("SELECT * FROM kanji_attempts")
    suspend fun getAllAttemptsKanji(): List<KanjiAttemptEntity>

}