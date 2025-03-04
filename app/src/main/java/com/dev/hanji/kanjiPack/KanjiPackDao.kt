package com.dev.hanji.kanjiPack

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.dev.hanji.kanji.KanjiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KanjiPackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPack(pack: KanjiPackEntity)

    @Upsert
    suspend fun upsertPack(pack: KanjiPackEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKanji(kanji: KanjiEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKanjiPackCrossRef(crossRef: List<KanjiPackCrossRef>)

    @Delete
    suspend fun deletePack(kanjiPack: KanjiPackEntity)

    @Transaction
    @Query("SELECT * FROM kanji_packs")
    fun getAllPacks(): Flow<List<KanjiPackEntity>>

    @Transaction
    @Query("SELECT * FROM kanji_packs where pack_id = :packId")
    fun getPackById(packId: Long): Flow<KanjiPackEntity>

    @Transaction
    @Query("SELECT * FROM kanji_packs")
    fun getAllPacksWithKanji(): Flow<List<PackWithKanji>>

    @Transaction
    @Query("SELECT * FROM kanji_packs WHERE pack_id = :packId")
    fun getKanjiListByPackId(packId: Long): Flow<PackWithKanji>

}

