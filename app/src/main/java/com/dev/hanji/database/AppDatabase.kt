package com.dev.hanji.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.hanji.achievements.AchievementDao
import com.dev.hanji.achievements.AchievementEntity
import com.dev.hanji.kanji.KanjiConverters
    import com.dev.hanji.kanji.KanjiDao
import com.dev.hanji.kanji.KanjiEntity
import com.dev.hanji.user.UserDao
import com.dev.hanji.user.UserEntity

@TypeConverters(value = [KanjiConverters::class])
@Database(entities = [UserEntity::class, AchievementEntity::class, KanjiEntity::class], version = 3, exportSchema = false)
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

// private val MIGRATION_1_2 = object : Migration(1, 2) {
//     override fun migrate(db: SupportSQLiteDatabase) {
//         db.execSQL("""
//             CREATE TABLE new_current_achievements (
//                 achievement_id INTEGER PRIMARY KEY NOT NULL,
//                 name TEXT NOT NULL,
//                 condition TEXT NOT NULL,
//                 is_completed INTEGER NOT NULL DEFAULT 0,
//                 character TEXT
//             )
//         """.trimIndent())
//
//         db.execSQL("""
//             INSERT INTO new_current_achievements (achievement_id, name, condition, is_completed)
//             SELECT achievement_id, name, condition, is_completed FROM current_achievements
//         """.trimIndent())
//
//         db.execSQL("DROP TABLE current_achievements")
//
//         db.execSQL("ALTER TABLE new_current_achievements RENAME TO current_achievements")
//     }
// }
//
// private val MIGRATION_2_3 = object : Migration(2, 3) {
//     override fun migrate(db: SupportSQLiteDatabase) {
//         db.execSQL("""
//             CREATE TABLE IF NOT EXISTS kanji (
//                 character TEXT PRIMARY KEY NOT NULL,
//                 strokes INTEGER NOT NULL DEFAULT 0,
//                 meanings TEXT NOT NULL,
//                 readings_on TEXT NOT NULL,
//                 readings_kun TEXT NOT NULL,
//                 unicode_hex TEXT NOT NULL
//             )
//         """.trimIndent())
//     }
// }
