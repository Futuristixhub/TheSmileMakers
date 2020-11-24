package com.smilemakers.ui.dashBoard.appointmentFragment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Area(
    @PrimaryKey(autoGenerate = false)
    val area_id: String,
    val area_name: String
)