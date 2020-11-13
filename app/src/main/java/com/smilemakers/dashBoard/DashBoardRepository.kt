package com.smilemakers.dashBoard

import com.smilemakers.db.AppDatabase
import com.smilemakers.db.entities.User
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest
import com.smilemakers.network.response.DashBoardResponse
import com.smilemakers.network.response.LoginResponse

class DashBoardRepository(
    private val myApi: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun dashBoardCount(userid: String): DashBoardResponse {
        return apiRequest { myApi.dashboardCount(userid) }
    }

}