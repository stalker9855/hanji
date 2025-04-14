package com.dev.hanji.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.hanji.data.model.KanjiAttemptEntity
import com.dev.hanji.data.model.KanjiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KanjiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kanji: List<KanjiEntity>)

    @Query("SELECT COUNT(*) FROM kanji")
    suspend fun getKanjiCount(): Int

    @Query("SELECT * FROM kanji")
    suspend fun getAllKanji(): List<KanjiEntity>

    @Query("SELECT * FROM kanji where character = :character")
    fun getKanjiByCharacter(character: String): Flow<KanjiEntity>

    @Query("SELECT * FROM kanji_attempts WHERE character = :character LIMIT 1")
    fun getAttemptKanjiByCharacter(character: String): Flow<KanjiAttemptEntity>



    @Query("""
    SELECT * FROM kanji 
    WHERE character LIKE '%' || :query || '%' 
       OR meanings LIKE '%' || :query || '%'
       OR readings_on LIKE '%' || :query || '%'
       OR readings_kun LIKE '%' || :query || '%'
        """)
    fun getKanjiWithPagination(query: String): PagingSource<Int, KanjiEntity>

}