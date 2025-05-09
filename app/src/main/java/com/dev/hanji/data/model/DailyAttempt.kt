package com.dev.hanji.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_attempts")
data class DailyAttempt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val date: String,

    @ColumnInfo("user_id")
    val userId: Int = 1,

    val attempts: Int = 0,

    @ColumnInfo("attempts_lines")
    val attemptsLines: Int = 0
)