package com.smilemakers.dashBoard.patientFragment

interface PatientListener {
    fun onStarted()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}