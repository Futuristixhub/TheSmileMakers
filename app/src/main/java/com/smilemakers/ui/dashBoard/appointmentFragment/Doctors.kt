package com.smilemakers.ui.dashBoard.appointmentFragment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Doctors(
    @PrimaryKey(autoGenerate = false)
    val doctor_id: String,
    val fname: String,
    val lname: String
)