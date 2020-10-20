package com.smilemakers.forgotPassword

import com.smilemakers.db.AppDatabase
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest

class ForgorPasswordRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getForgotPasswordData() = apiRequest { api.getForgotPasswordData() }

}
