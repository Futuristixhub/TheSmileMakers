package com.smilemakers.ui.dashBoard.appointmentFragment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Treatments(
    @PrimaryKey(autoGenerate = false)
    val treatment_id: String,
    val treatment_name: String
)