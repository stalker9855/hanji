package com.dev.hanji.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.hanji.data.dao.ProgressDao
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
    ], version = 4,
    autoMigrations = [AutoMigration(from = 3, to = 4)]
    )
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val progressDao: ProgressDao
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
                       .createFromAsset("databases/hanji_database.db")
                       .fallbackToDestructiveMigrationOnDowngrade()
                       .build()
                   INSTANCE = instance
               }
               return instance
           }
        }
    }
}

