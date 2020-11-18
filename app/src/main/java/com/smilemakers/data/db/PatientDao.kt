package com.smilemakers.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smilemakers.ui.dashBoard.patientFragment.Patient

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllPatients(patients : List<Patient>)

    @Query("SELECT * FROM Patient")
    fun getPatients() : LiveData<List<Patient>>
}
