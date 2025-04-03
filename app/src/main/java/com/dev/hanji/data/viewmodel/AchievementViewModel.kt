package com.dev.hanji.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.hanji.data.model.AchievementsProvider
import com.dev.hanji.data.dao.AchievementDao
import com.dev.hanji.data.model.AchievementEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AchievementViewModel(private val achievementDao: AchievementDao): ViewModel() {
    private val _achievements = MutableStateFlow<List<AchievementEntity>>(emptyList())
    val achievements: StateFlow<List<AchievementEntity>>
        get() = _achievements

    val completedAchievements: StateFlow<Int> = _achievements.map {
        achievementsList -> achievementsList.count { it.isCompleted}
    }.stateIn(
        viewModelScope, SharingStarted.Lazily, 0
    )


    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        viewModelScope.launch {
            _achievements.value = AchievementsProvider.achievements
        }
    }
}