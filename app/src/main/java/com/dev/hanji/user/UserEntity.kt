package com.dev.hanji.user

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.hanji.ui.theme.ErrorAttemptColor
import com.dev.hanji.ui.theme.GoodAttemptColor
import com.dev.hanji.ui.theme.GreatAttemptColor
import com.dev.hanji.ui.theme.NormalAttemptColor

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
    val normalAttempts: Int,

    @ColumnInfo(name = "error_attempts")
    val errorAttempts: Int,

) {
    val attempts: List<Pair<Int?, Color>>
        get() = listOf(
            Pair(greatAttempts, GreatAttemptColor),
            Pair(goodAttempts, GoodAttemptColor),
            Pair(normalAttempts, NormalAttemptColor),
            Pair(errorAttempts, ErrorAttemptColor),
        )
}
