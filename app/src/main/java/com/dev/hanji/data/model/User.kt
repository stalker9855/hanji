package com.dev.hanji.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "current_user")
data class UserEntity (

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val id : Int = 1,

    val username: String,
    val email: String,

    @ColumnInfo(name = "great_attempts")
    val greatAttempts: Int = 0,

    @ColumnInfo(name = "good_attempts")
    val goodAttempts: Int = 0,

    @ColumnInfo(name = "normal_attempts")
    val normalAttempts: Int = 0,

    @ColumnInfo(name = "failed_attempts")
    val failedAttempts: Int = 0,

    )
