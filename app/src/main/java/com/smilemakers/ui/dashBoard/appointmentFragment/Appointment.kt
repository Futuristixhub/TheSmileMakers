package com.smilemakers.ui.dashBoard.appointmentFragment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Appointment(
    @PrimaryKey(autoGenerate = false)
    val appointment_id: String,
    val appointment_type: String,
    val fname: String,
    val lname: String,
    val appointment_date: String,
    val appointment_time: String,
    val typesoftreatment: String,
    val prescription: String,
    val doctor_id: String,
    val color: String,
    val age: String
)