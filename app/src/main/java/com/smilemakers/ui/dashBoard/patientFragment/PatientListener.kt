package com.smilemakers.ui.dashBoard.patientFragment

interface PatientListener {
    fun onStarted()
    fun onSuccess(message: String, value: String="")
    fun onFailure(message: String)
}