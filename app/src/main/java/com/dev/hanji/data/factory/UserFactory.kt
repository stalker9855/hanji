package com.dev.hanji.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hanji.data.DataStoreRepository
import com.dev.hanji.data.dao.UserDao
import com.dev.hanji.data.viewmodel.UserViewModel

class UserFactory(private val userDao: UserDao, private val repository: DataStoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(userDao, repository) as T
        } else {
            throw IllegalArgumentException("Unknown model")
        }
    }
}