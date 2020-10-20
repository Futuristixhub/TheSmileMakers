package com.smilemakers.dashBoard.patientFragment

import com.smilemakers.db.AppDatabase
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest

class PatientRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getPatientData() = apiRequest { api.getPatientData() }

}
