package com.smilemakers.dashBoard.profile

import com.smilemakers.db.AppDatabase
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest

class ProfileRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getProfileData() = apiRequest { api.getProfileData("") }

}
