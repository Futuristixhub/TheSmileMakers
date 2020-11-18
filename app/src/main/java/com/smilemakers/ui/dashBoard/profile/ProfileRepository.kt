package com.smilemakers.ui.dashBoard.profile

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest

class ProfileRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getProfileData() = apiRequest { api.getProfileData("") }

}
