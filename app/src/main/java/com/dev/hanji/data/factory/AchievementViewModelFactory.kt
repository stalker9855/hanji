package com.dev.hanji.data.factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hanji.data.dao.AchievementDao
import com.dev.hanji.data.viewmodel.AchievementViewModel

class AchievementViewModelFactory(private val achievementDao: AchievementDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AchievementViewModel::class.java)) {
            AchievementViewModel(achievementDao) as T
        } else {
            throw IllegalArgumentException("Unknown model")
        }
    }
}
