package com.smilemakers.ui.dashBoard.appointmentFragment

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest
import com.smilemakers.data.network.response.PatientResponse
import okhttp3.MultipartBody

class AppointmentRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getAppointmentData(userid: String?,userType:String?) = apiRequest { api.getAppointmentData(userid!!,userType!!) }

    suspend fun addAppointment(
        patient_id: String,
        ftname: String,
        retarea: String,
        apptdate: String,
        timee: String,
        typesoftreatment: String,
        doctor: String,
        color: String
        ): PatientResponse {
        return apiRequest {
            api.addAppointment(
               patient_id,ftname,retarea,apptdate,timee,typesoftreatment,doctor,color
            )
        }
    }
}
