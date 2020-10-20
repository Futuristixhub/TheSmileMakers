package com.smilemakers.dashBoard.appointmentFragment

import com.smilemakers.db.AppDatabase
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest

class AppointmentRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getAppointmentData() = apiRequest { api.getAppointmentData() }

}
