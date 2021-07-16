package com.smilemakers.ui.dashBoard.appointmentFragment

import com.smilemakers.ui.dashBoard.patientFragment.Datacls

data class AppointmentData (
    val status: Boolean?,
    val appointment_list: List<Appointment>,
    val message: String
)