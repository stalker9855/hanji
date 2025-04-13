package com.dev.hanji.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hanji.data.dao.UserDao
import com.dev.hanji.data.viewmodel.UserViewModel

class UserViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(userDao) as T
        } else {
            throw IllegalArgumentException("Unknown model")
        }
    }
}