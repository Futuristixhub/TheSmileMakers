package com.smilemakers.dashBoard.doctorFragment

import com.smilemakers.db.AppDatabase
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest

class DoctorRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getDoctorData() = apiRequest { api.getDoctorData() }

}
