package com.dev.hanji.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dev.hanji.achievements.AchievementDao
import com.dev.hanji.achievements.AchievementEntity
import com.dev.hanji.user.UserDao
import com.dev.hanji.user.UserEntity

@Database(entities = [UserEntity::class, AchievementEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val achievementDao: AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
           synchronized(this)  {
               var instance = INSTANCE
               if(instance == null) {
                   instance = Room.databaseBuilder(
                       context.applicationContext,
                       AppDatabase::class.java,
                       "hanji_database"
                   ).build()
                   INSTANCE = instance
               }
               return instance
           }
        }
    }
}