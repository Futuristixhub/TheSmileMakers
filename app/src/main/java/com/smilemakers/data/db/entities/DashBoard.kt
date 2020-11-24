package com.smilemakers.data.db.entities

import androidx.room.Entity

@Entity
data class DashBoard(
    var total_patient: String? = null,
    var total_doctor: String? = null,
    var total_admin: String? = null,
    var total_bapunagar: String? = null,
    var total_nikol: String? = null
)