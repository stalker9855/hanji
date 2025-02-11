package com.dev.hanji.achievements
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AchievementViewModelFactory(private val achievementDao: AchievementDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AchievementViewModel::class.java)) {
            AchievementViewModel(achievementDao) as T
        } else {
            throw IllegalArgumentException("Unknown model")
        }
    }
}
