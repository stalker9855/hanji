package com.dev.hanji.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.hanji.data.DataStoreRepository
import com.dev.hanji.data.dao.UserDao
import com.dev.hanji.data.events.UserEvent
import com.dev.hanji.data.model.UserEntity
import com.dev.hanji.data.state.AttemptState
import com.dev.hanji.data.state.AttemptWithColor
import com.dev.hanji.data.state.OnBoardUserState
import com.dev.hanji.data.state.TypeAttempt
import com.dev.hanji.data.state.UserAttempt
import com.dev.hanji.data.state.UserState
import com.dev.hanji.ui.theme.BadAttemptColor
import com.dev.hanji.ui.theme.GoodAttemptColor
import com.dev.hanji.ui.theme.CleanAttemptColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UserViewModel(private val dao: UserDao, private val repository: DataStoreRepository) : ViewModel() {
    private val _state = MutableStateFlow(UserState())

    private val _onBoardState = MutableStateFlow(OnBoardUserState())

    private val _user: Flow<UserEntity> = dao.getUser()
    private val _attempts: Flow<AttemptState> = dao.getAttempts()

    private val _attemptsWithColor: Flow<List<UserAttempt>> = _attempts
        .map { attemptState ->
            listOf(
                AttemptWithColor(attemptState.clean, CleanAttemptColor, type = TypeAttempt.CLEAN),
                AttemptWithColor(attemptState.good, GoodAttemptColor, type = TypeAttempt.GOOD),
                AttemptWithColor(attemptState.bad, BadAttemptColor, type = TypeAttempt.BAD),
            )
        }

    val state = combine(_state, _user, _attemptsWithColor) {
        state, user, attempts ->
            state.copy(
                attempts = attempts,
                user = user,
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserState())

    val onBoardUserState = _onBoardState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OnBoardUserState())

    // X3


    fun onEvent(event: UserEvent) {
        when(event) {
            is UserEvent.SetUsername -> {
                _onBoardState.update {
                    it.copy(
                        username = event.username
                    )
                }
            }

            is UserEvent.InsertUser -> {
                val username = onBoardUserState.value.username
                val user = UserEntity(
                    username = username,
                    email = "bob@mail.com"
                )
                viewModelScope.launch {
                    dao.insert(user)
                }
            }

            is UserEvent.LoadAvatar -> {
                viewModelScope.launch {
                    repository.readAvatar().collect { uriString ->
                        if (uriString != null) {
                            _onBoardState.update { currentState ->
                                currentState.copy(avatar = uriString)
                            }
                            _state.update { currentState ->
                                currentState.copy(avatar = uriString)
                            }
                        }
                    }
                }
            }

            is UserEvent.UpdateAvatar -> {
                    _state.update { currentState ->
                        currentState.copy(avatar = event.uri)
                    }
                    _onBoardState.update { currentState ->
                        currentState.copy(avatar = event.uri)
                    }
                    viewModelScope.launch {
                        repository.saveAvatar(event.uri)
                    }
                }
            }
        }
    }



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
