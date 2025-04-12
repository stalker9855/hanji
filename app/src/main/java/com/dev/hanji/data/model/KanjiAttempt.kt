package com.dev.hanji.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation

@Entity(
    tableName = "kanji_attempts",
    primaryKeys = ["user_id", "character"],
    foreignKeys = [
        ForeignKey(
            entity = KanjiEntity::class,
            parentColumns = ["character"],
            childColumns = ["character"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class KanjiAttemptEntity(

    @ColumnInfo("character")
    val character: String,

    @ColumnInfo("user_id")
    val userId: Long,

    val attempts: Long = 0,

    @ColumnInfo(defaultValue = "0")
    val clean: Long = 0,

    val errors: Long = 0,

    @ColumnInfo("e_factor")
    val eFactor: Double = 2.5,

    val interval: Int = 1,

    @ColumnInfo("last_review")
    val lastReview: Long = System.currentTimeMillis(),
    @ColumnInfo("next_review_data")
    val nextReviewDate: Long = System.currentTimeMillis() + 24 * 60 * 60 * 1000
)

data class KanjiAttemptWithUser(
    @Embedded val kanjiAttempt: KanjiAttemptEntity,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    val user: UserEntity,

    @Relation(
        parentColumn = "character",
        entityColumn = "character"
    )
    val kanjiDetails: KanjiEntity
)


