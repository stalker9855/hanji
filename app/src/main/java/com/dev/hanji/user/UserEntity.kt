package com.dev.hanji.user

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.hanji.ui.theme.ErrorAttemptColor
import com.dev.hanji.ui.theme.GoodAttemptColor
import com.dev.hanji.ui.theme.GreatAttemptColor
import com.dev.hanji.ui.theme.NormalAttemptColor

enum class TypeAttempt(val value: String) {
     BAD("Bad"),
     NORMAL("Normal"),
     GOOD("Good"),
     GREAT("Great")
}

interface UserAttempt {
    val type: TypeAttempt
    val count: Int
    val color: Color
}

data class ConcreteUserAttempt (
    override val type: TypeAttempt,
    override val count: Int,
    override val color: Color
) : UserAttempt

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

    @ColumnInfo(name = "normal_attempts")
    val normalAttempts: Int,

    @ColumnInfo(name = "failed_attempts")
    val failedAttempts: Int,

    ) {
    val attempts: List<UserAttempt>
        get() = listOf(
            ConcreteUserAttempt(TypeAttempt.GREAT, greatAttempts, GreatAttemptColor),
            ConcreteUserAttempt(TypeAttempt.GOOD, goodAttempts, GoodAttemptColor),
            ConcreteUserAttempt(TypeAttempt.NORMAL, normalAttempts, NormalAttemptColor),
            ConcreteUserAttempt(TypeAttempt.BAD, failedAttempts, ErrorAttemptColor),
        )
}
