package com.smilemakers.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smilemakers.ui.dashBoard.doctorFragment.Doctor

@Dao
interface DoctorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllDoctors(doctors : List<Doctor>)

    @Query("SELECT * FROM Doctor")
    fun getDoctors() : LiveData<List<Doctor>>
}
