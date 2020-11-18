package com.smilemakers.data.db.entities

import androidx.room.Entity

@Entity
data class DashBoard(
    var total_patient: String? = null,
    var total_doctor: String? = null,
    var total_admin: String? = null
)