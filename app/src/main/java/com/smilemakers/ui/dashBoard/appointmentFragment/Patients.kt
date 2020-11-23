package com.smilemakers.ui.dashBoard.appointmentFragment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Patients(
    @PrimaryKey(autoGenerate = false)
    val patient_id: String,
    val fname: String,
    val lname: String
)