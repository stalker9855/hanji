package com.dev.hanji.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.hanji.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserEntity)

    @Query("UPDATE current_user SET great_attempts = :great, good_attempts = :good, normal_attempts = :normal, failed_attempts = :failed WHERE user_id = 1")
    suspend fun updateUserAttempts(great: Int, good: Int, normal: Int, failed: Int)

    @Query("SELECT * FROM current_user LIMIT 1")
    fun get(): Flow<UserEntity?>

    @Delete
    suspend fun delete(user: UserEntity)

}