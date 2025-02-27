package com.dev.hanji.kanjiPack

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.dev.hanji.kanji.KanjiEntity


@Entity(tableName = "kanji_packs")
data class KanjiPackEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("description")
    val description: String,
)

@Entity(
    tableName = "kanji_pack_cross_ref",
    primaryKeys = ["pack_id", "kanji_character"],
    foreignKeys = [
        ForeignKey(
            entity = KanjiPackEntity::class,
            parentColumns = ["id"],
            childColumns = ["pack_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = KanjiEntity::class,
            parentColumns = ["character"],
            childColumns = ["kanji_character"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class KanjiPackCrossRef(
    @ColumnInfo("pack_id")
    val packId: Int,

    @ColumnInfo("kanji_character")
    val character: String
)

data class KanjiWithPacks(
    @Embedded val kanji: KanjiEntity,
    @Relation(
        parentColumn = "character",
        entityColumn = "pack_id",
        associateBy = Junction(KanjiPackCrossRef::class)
    )
    val packs: List<KanjiPackEntity>
)

data class PackWithKanji(
    @Embedded val pack: KanjiPackEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "kanji_character",
        associateBy = Junction(KanjiPackCrossRef::class)
    )
    val kanjiList: List<KanjiEntity>
)
