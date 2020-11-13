package com.smilemakers.dashBoard.patientFragment

import com.smilemakers.db.AppDatabase
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest
import com.smilemakers.network.response.DashBoardResponse
import com.smilemakers.network.response.LoginResponse

class PatientRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getPatientData() = apiRequest { api.getPatientData("") }

    suspend fun addPatient(
        fname: String,
        lname: String,
        gender: String,
        dob: String,
        age: String,
        refid: String,
        refname: String,
        mNo: String,
        location: String,
        adr1: String,
        adr2: String,
        city: String,
        state: String,
        country: String,
        pincode: String
    ): DashBoardResponse {
        return apiRequest {
            api.addPatient(
                fname,
                lname,
                gender,
                dob,
                age,
                refid,
                refname,
                mNo,
                location,
                adr1,
                adr2,
                city,
                state,
                country,
                pincode
            )
        }
    }
}
