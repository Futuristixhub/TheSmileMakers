package com.smilemakers.ui.dashBoard.patientFragment


data class PatientData(
    val status: Boolean?,
    val patient_list: List<Patient>,
    val message: String
)