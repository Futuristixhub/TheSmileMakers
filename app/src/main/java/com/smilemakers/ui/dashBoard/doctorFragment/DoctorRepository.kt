package com.smilemakers.ui.dashBoard.doctorFragment

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest

class DoctorRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getDoctorData(userid: String?)= apiRequest { api.getDoctorData(userid!!) }

}
