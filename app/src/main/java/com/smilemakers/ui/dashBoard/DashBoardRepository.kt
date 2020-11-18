package com.smilemakers.ui.dashBoard

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest
import com.smilemakers.data.network.response.DashBoardResponse

class DashBoardRepository(
    private val myApi: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun dashBoardCount(userid: String): DashBoardResponse {
        return apiRequest { myApi.dashboardCount(userid) }
    }

}