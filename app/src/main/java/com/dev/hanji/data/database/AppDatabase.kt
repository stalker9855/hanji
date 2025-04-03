package com.dev.hanji.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.hanji.data.dao.AchievementDao
import com.dev.hanji.data.model.AchievementEntity
import com.dev.hanji.data.dao.KanjiAttemptDao
import com.dev.hanji.data.model.KanjiAttemptEntity
import com.dev.hanji.data.model.KanjiConverters
import com.dev.hanji.data.dao.KanjiDao
import com.dev.hanji.data.model.KanjiEntity
import com.dev.hanji.data.model.KanjiPackCrossRef
import com.dev.hanji.data.dao.KanjiPackDao
import com.dev.hanji.data.model.KanjiPackEntity
import com.dev.hanji.data.dao.UserDao
import com.dev.hanji.data.model.UserEntity

@TypeConverters(value = [KanjiConverters::class])
@Database(entities = [
    UserEntity::class,
    AchievementEntity::class,
    KanjiEntity::class,
    KanjiPackEntity::class,
    KanjiPackCrossRef::class,
    KanjiAttemptEntity::class,
    ], version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
    )
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val achievementDao: AchievementDao
    abstract val kanjiDao: KanjiDao
    abstract val kanjiPackDao: KanjiPackDao
    abstract val kanjiAttemptDao: KanjiAttemptDao

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

