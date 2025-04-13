package com.dev.hanji.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.dev.hanji.data.model.KanjiAttemptEntity
import com.dev.hanji.data.model.KanjiEntity
import com.dev.hanji.data.model.KanjiPackCrossRef
import com.dev.hanji.data.model.KanjiPackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KanjiAttemptDao {

    @Upsert
    suspend fun insert(kanjiAttempt: KanjiAttemptEntity)

    @Query("SELECT * FROM kanji_attempts")
    fun getAllAttemptsKanji(): Flow<List<KanjiAttemptEntity>>

    @Query("""
    SELECT k.* FROM kanji k
    INNER JOIN kanji_attempts a ON k.character = a.character
    WHERE a.user_id = :userId
      AND DATE(a.next_review_data / 1000, 'unixepoch') >= DATE('now', '-2 day')
      AND a.next_review_data > a.last_review
    """)
    fun getKanjiDueTomorrow(userId: Long): Flow<List<KanjiEntity>>

    @Query("SELECT * FROM kanji_attempts WHERE character = :character LIMIT 1")
    suspend fun getKanjiByCharacter(character: String): KanjiAttemptEntity?

    @Insert
    suspend fun insertKanjiPack(kanjiPack: KanjiPackEntity): Long

    @Update
    suspend fun update(attempt: KanjiAttemptEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKanjiPackCrossRef(crossRef: List<KanjiPackCrossRef>)

    @Upsert
    suspend fun upsertKanjiPackCrossRef(crossRef: List<KanjiPackCrossRef>)
}