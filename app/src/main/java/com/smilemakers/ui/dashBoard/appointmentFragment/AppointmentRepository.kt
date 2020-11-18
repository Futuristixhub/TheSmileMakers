package com.smilemakers.ui.dashBoard.appointmentFragment

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest

class AppointmentRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getAppointmentData() = apiRequest { api.getAppointmentData("") }

}
