package com.smilemakers.ui.forgotPassword

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest

class ForgorPasswordRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getForgotPasswordData() = apiRequest { api.getForgotPasswordData() }

}
