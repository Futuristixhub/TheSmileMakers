package com.smilemakers.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smilemakers.data.db.entities.CURRENT_USER_ID
import com.smilemakers.data.db.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User): Long

    @Query("SELECT * FROM user WHERE uid=$CURRENT_USER_ID")
    fun getUser():LiveData<User>

}