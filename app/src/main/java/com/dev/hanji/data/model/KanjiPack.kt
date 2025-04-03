package com.dev.hanji.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(
    tableName = "kanji_packs",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"]
        )
    ]

    )
data class KanjiPackEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("pack_id")
    val id: Long = 0,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("user_id")
    val userId: Int
)

@Entity(
    tableName = "kanji_pack_cross_ref",
    primaryKeys = ["pack_id", "character"],
    foreignKeys = [
        ForeignKey(
            entity = KanjiPackEntity::class,
            parentColumns = ["pack_id"],
            childColumns = ["pack_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = KanjiEntity::class,
            parentColumns = ["character"],
            childColumns = ["character"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class KanjiPackCrossRef(
    @ColumnInfo("pack_id")
    val packId: Long,

    @ColumnInfo("character")
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
        parentColumn = "pack_id",
        entityColumn = "character",
        associateBy = Junction(KanjiPackCrossRef::class)
    )
    val kanjiList: List<KanjiEntity>
) {
    val kanjiCount: Int
        get() = kanjiList.size
}
