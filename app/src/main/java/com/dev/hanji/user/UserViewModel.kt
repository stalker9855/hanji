package com.dev.hanji.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class UserViewModel(private val userDao: UserDao) : ViewModel() {
   private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?>
        get() = _user

    val totalAttempts: StateFlow<Int> = _user.map { user ->
        user?.let {
            it.greatAttempts + it.goodAttempts + it.normalAttempts + it.errorAttempts
        } ?: 0
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _user.value = getFakeUser()
            // userDao.get().collect() {
            //     // userEntity -> _user.value = userEntity
            // }
        }
    }

    private  fun getFakeUser(): UserEntity {
        return UserEntity(
            id = 1,
            username = "stalker9855",
            email = "bobross@example.com",
            greatAttempts = 200,
            goodAttempts = 400,
            normalAttempts = 180,
            errorAttempts = 128
        )
    }
}