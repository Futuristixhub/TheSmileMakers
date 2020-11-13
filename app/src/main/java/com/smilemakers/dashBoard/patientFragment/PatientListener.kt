package com.smilemakers.dashBoard.patientFragment

import com.smilemakers.db.entities.DashBoard

interface PatientListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}