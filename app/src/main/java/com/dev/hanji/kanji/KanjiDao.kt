package com.dev.hanji.kanji

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KanjiDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(kanji: List<KanjiEntity>)

    @Query("SELECT COUNT(*) FROM kanji")
    suspend fun getKanjiCount(): Int
}