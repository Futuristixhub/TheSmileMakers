package com.smilemakers.ui.dashBoard.doctorFragment

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest
import com.smilemakers.data.network.response.PatientResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DoctorRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getDoctorData(userid: String?)= apiRequest { api.getDoctorData(userid!!) }

    suspend fun addDoctor(
        fname: RequestBody,
        lname: RequestBody,
        gender: RequestBody,
        dob: RequestBody,
        age: RequestBody,
        email: RequestBody,
        education: RequestBody,
        mNo: RequestBody,
        altmNo: RequestBody,
        location: RequestBody,
        trtmentType: RequestBody,
        adr1: RequestBody,
        adr2: RequestBody,
        city: RequestBody,
        state: RequestBody,
        country: RequestBody,
        pincode: RequestBody,
        image: MultipartBody.Part,
        requestBody: RequestBody
    ): PatientResponse {
        return apiRequest {
            api.addDoctor(
                fname,
                lname,
                gender,
                dob,
                age,
                email,
                education,
                mNo,
                altmNo,
                location,
                trtmentType,
                adr1,
                adr2,
                city,
                state,
                country,
                pincode,
                image,requestBody
            )
        }
    }

}
