package com.dev.hanji.achievements

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.hanji.R

enum class TypeAchievement(val value: String) {
    TOTAL_KANJI("Total Kanji"),
    LOGIN_DAY("Login count day"),
}

@Entity(tableName = "current_achievements")
data class AchievementEntity (
    @PrimaryKey
    @ColumnInfo(name = "achievement_id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "condition")
    val condition: String,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "character")
    val character: String?
)