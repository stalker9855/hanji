package com.dev.hanji.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.hanji.data.model.KanjiEntity

@Dao
interface KanjiDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(kanji: List<KanjiEntity>)

    @Query("SELECT COUNT(*) FROM kanji")
    suspend fun getKanjiCount(): Int

    @Query("SELECT * FROM kanji")
    suspend fun getAllKanji(): List<KanjiEntity>

}