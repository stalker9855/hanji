package com.dev.hanji.data.state

import com.dev.hanji.data.model.UserEntity


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

data class UserState (
    val userId: Int = 0,
    val username: String = "",
    val email: String = "",
    val user: UserEntity? = null,
    val attempts: AttemptState? = null
)