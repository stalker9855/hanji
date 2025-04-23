package com.dev.hanji.data.state

import androidx.compose.ui.graphics.Color
import com.dev.hanji.data.model.UserEntity


enum class TypeAttempt(val value: String) {
    CLEAN("Clean"),
    GOOD("Good"),
    BAD("Bad"),
    ATTEMPT_LINE("Attempts (lines)"),
    FAIL_LINE("Failed (lines)"),
}

interface UserAttempt {
    val attempt: Long?
    val color: Color
    val type: TypeAttempt
}

interface HasAttempts {
    val attempts: List<UserAttempt>?
    val total: Long
}

data class AttemptState(
    val attempts: Long = 0,
    val clean: Long = 0,
    val good: Long = 0,
    val bad: Long = 0,
    val errors: Long = 0,
){
    val total: Long
        get() = attempts + clean + good + errors + bad
}

data class AttemptWithColor(
    override val attempt: Long?,
    override val color: Color,
    override val type: TypeAttempt
) : UserAttempt

data class OnBoardUserState(
    val username: String = "",
     val avatar: String? = null
)


data class UserState (
    val userId: Int = 0,
    val avatar: String? = null,
    val username: String = "",
    val email: String = "",
    val user: UserEntity? = null,
    override val attempts: List<UserAttempt>? = null
): HasAttempts {
    override val total: Long
        get() = attempts?.sumOf { it.attempt ?: 0 } ?: 0

}

