package com.dev.hanji.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.data.dao.UserDao
import com.dev.hanji.data.model.UserEntity
import com.dev.hanji.data.state.AttemptState
import com.dev.hanji.data.state.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class UserViewModel(dao: UserDao) : ViewModel() {
    private val _state = MutableStateFlow(UserState())

    private val _user: Flow<UserEntity> = dao.getUser()
    private val _attempts: Flow<AttemptState> = dao.getAttempts()

    val state = combine(_state, _user, _attempts) {
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