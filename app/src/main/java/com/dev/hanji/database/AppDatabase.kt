package com.dev.hanji.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.hanji.achievements.AchievementDao
import com.dev.hanji.achievements.AchievementEntity
import com.dev.hanji.kanji.KanjiConverters
import com.dev.hanji.kanji.KanjiDao
import com.dev.hanji.kanji.KanjiEntity
import com.dev.hanji.kanjiPack.KanjiPackCrossRef
import com.dev.hanji.kanjiPack.KanjiPackEntity
import com.dev.hanji.user.UserDao
import com.dev.hanji.user.UserEntity

@TypeConverters(value = [KanjiConverters::class])
@Database(entities = [
    UserEntity::class,
    AchievementEntity::class,
    KanjiEntity::class,
    KanjiPackEntity::class,
    KanjiPackCrossRef::class], version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
    )
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val achievementDao: AchievementDao
    abstract val kanjiDao: KanjiDao

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
                   )
                       .build()
                   INSTANCE = instance
               }
               return instance
           }
        }
    }
}

