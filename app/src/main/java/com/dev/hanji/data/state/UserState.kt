package com.dev.hanji.data.state

import androidx.compose.ui.graphics.Color
import com.dev.hanji.data.model.UserEntity


enum class TypeAttempt(val value: String) {
    BAD("Bad"),
    NORMAL("Attempts (on lines)"),
    GOOD("Good"),
    GREAT("Great"),
    ERROR("Failed")
}

interface UserAttempt {
    val attempt: Int
    val color: Color
    val type: TypeAttempt
}


data class AttemptState(
    val attempts: Int = 0,
    val clean: Int = 0,
    val good: Int = 0,
    val bad: Int = 0,
    val errors: Int = 0,
){
    val total: Int
        get() = attempts + clean + good + errors + bad
}

data class AttemptWithColor(
    override val attempt: Int,
    override val color: Color,
    override val type: TypeAttempt
) : UserAttempt

data class UserState (
    val userId: Int = 0,
    val username: String = "",
    val email: String = "",
    val user: UserEntity? = null,
    val attempts: List<UserAttempt>? = null
) {
    val total: Int
        get() = attempts?.sumOf { it.attempt } ?: 0

}