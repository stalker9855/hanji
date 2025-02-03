package com.dev.hanji.user

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
    val greatAttempts: Int,

    @ColumnInfo(name = "good_attempts")
    val goodAttempts: Int,

    @ColumnInfo(name = "bad_attempts")
    val badAttempts: Int,

    @ColumnInfo(name = "error_attempts")
    val errorAttempts: Int,

) {
    val attempts: Int
        get() = greatAttempts + goodAttempts + badAttempts + errorAttempts
}
