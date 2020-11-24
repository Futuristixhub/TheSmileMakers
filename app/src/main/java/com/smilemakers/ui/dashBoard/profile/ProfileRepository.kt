package com.smilemakers.ui.dashBoard.profile

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest
import com.smilemakers.data.network.response.PatientResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getProfileData(userid: String, utype: String): ProfileData {
        return apiRequest { api.getProfileData(userid,utype) }
    }

    suspend fun editProfile(
        userid: RequestBody,
        usertype: RequestBody,
        fname: RequestBody,
        lname: RequestBody,
        email: RequestBody,
        mobile: RequestBody,
        address: RequestBody,
        image: MultipartBody.Part,
        requestBody: RequestBody
    ): PatientResponse {
        return apiRequest {
            api.editProfile(
                userid,
                usertype,
                fname,
                lname,
                email,
                mobile,
                address,
                image,requestBody
            )
        }
    }
}
