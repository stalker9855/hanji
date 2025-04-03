package com.dev.hanji.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.data.dao.UserDao
import com.dev.hanji.data.model.UserEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class UserViewModel(userDao: UserDao) : ViewModel() {
    private val fakeUser = UserEntity(
        username = "bobross",
        email = "bob@mail.com",
        greatAttempts = 100,
        goodAttempts = 50,
        normalAttempts = 40,
        failedAttempts = 2
    )
    private val _user = userDao.get()
        .map { it ?: fakeUser }
        .stateIn(viewModelScope, SharingStarted.Lazily, fakeUser)

    val user: StateFlow<UserEntity>
        get() = _user

    val totalAttempts: StateFlow<Int> = _user.map { user ->
        user.let {
            it.greatAttempts + it.goodAttempts + it.normalAttempts + it.failedAttempts
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

}