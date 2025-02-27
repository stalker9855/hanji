package com.dev.hanji.kanjiPack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dev.hanji.kanji.KanjiEntity

@Dao
interface KanjiPackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPack(pack: KanjiPackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKanji(kanji: KanjiEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKanjiPackCrossRef(crossRef: KanjiPackCrossRef)

    @Transaction
    @Query("SELECT * FROM kanji_packs WHERE id = :packId")
    suspend fun getPackWithKanji(packId: Int): List<PackWithKanji>

    @Transaction
    @Query("SELECT * FROM kanji WHERE character = :kanjiCharacter")
    suspend fun getKanjiWithPacks(kanjiCharacter: String): List<KanjiWithPacks>
}

