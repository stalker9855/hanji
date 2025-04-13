package com.dev.hanji.data.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.data.dao.UserDao
import com.dev.hanji.data.model.UserEntity
import com.dev.hanji.data.state.AttemptState
import com.dev.hanji.data.state.AttemptWithColor
import com.dev.hanji.data.state.TypeAttempt
import com.dev.hanji.data.state.UserAttempt
import com.dev.hanji.data.state.UserState
import com.dev.hanji.ui.theme.BadAttemptColor
import com.dev.hanji.ui.theme.GoodAttemptColor
import com.dev.hanji.ui.theme.CleanAttemptColor
import com.dev.hanji.ui.theme.NormalAttemptColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class UserViewModel(dao: UserDao) : ViewModel() {
    private val _state = MutableStateFlow(UserState())

    private val _user: Flow<UserEntity> = dao.getUser()
    private val _attempts: Flow<AttemptState> = dao.getAttempts()

    private val _attemptsWithColor: Flow<List<UserAttempt>> = _attempts
        .map { attemptState ->
            listOf(
                AttemptWithColor(attemptState.attempts, NormalAttemptColor, type = TypeAttempt.NORMAL),
                AttemptWithColor(attemptState.clean, CleanAttemptColor, type = TypeAttempt.GREAT),
                AttemptWithColor(attemptState.good, GoodAttemptColor, type = TypeAttempt.GOOD),
                AttemptWithColor(attemptState.bad, BadAttemptColor, type = TypeAttempt.BAD),
                AttemptWithColor(attemptState.errors, Color.Black, type = TypeAttempt.ERROR)
            )
        }

    val state = combine(_state, _user, _attemptsWithColor) {
        state, user, attempts ->
            state.copy(
                attempts = attempts,
                user = user,
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserState())



//    private val fakeUser = UserEntity(
//        username = "bobross",
//        email = "bob@mail.com",
//        greatAttempts = 100,
//        goodAttempts = 50,
//        normalAttempts = 40,
//        failedAttempts = 2
//    )
//    private val _user = userDao.get()
//        .map { it ?: fakeUser }
//        .stateIn(viewModelScope, SharingStarted.Lazily, fakeUser)
//
//    val user: StateFlow<UserEntity>
//        get() = _user
//
//    val totalAttempts: StateFlow<Int> = _user.map { user ->
//        user.let {
//            it.greatAttempts + it.goodAttempts + it.normalAttempts + it.failedAttempts
//        }
//    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

}