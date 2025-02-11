package com.dev.hanji.achievements

import com.dev.hanji.R

object AchievementsProvider {
    val achievements = listOf(
        AchievementEntity(
            id = 1,
            name = TypeAchievement.TOTAL_KANJI.value,
            condition = "Learn 10 kanji",
            isCompleted = false,
            imageResourceId = R.drawable.achievement1
        ),
        AchievementEntity(
            id = 2,
            name = TypeAchievement.LOGIN_DAY.value,
            condition = "Login for 7 days in a row",
            isCompleted = true,
            imageResourceId = R.drawable.achievement1
        ),
        AchievementEntity(
            id = 3,
            name = TypeAchievement.LOGIN_DAY.value,
            condition = "EXAMPLE",
            isCompleted = true,
            imageResourceId = R.drawable.achievement1
        ),
        AchievementEntity(
            id = 4,
            name = TypeAchievement.LOGIN_DAY.value,
            condition = "EXAMPLE",
            isCompleted = false,
            imageResourceId = R.drawable.achievement1
        ),
        AchievementEntity(
            id = 5,
            name = TypeAchievement.LOGIN_DAY.value,
            condition = "EXAMPLE",
            isCompleted = true,
            imageResourceId = R.drawable.achievement1
        ),
        AchievementEntity(
            id = 6,
            name = TypeAchievement.LOGIN_DAY.value,
            condition = "EXAMPLE",
            isCompleted = true,
            imageResourceId = R.drawable.achievement1
        ),
        AchievementEntity(
            id = 7,
            name = TypeAchievement.LOGIN_DAY.value,
            condition = "EXAMPLE",
            isCompleted = false,
            imageResourceId = R.drawable.achievement1
        ),
    )
}