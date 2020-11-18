package com.smilemakers.ui.login

import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.db.entities.User
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest
import com.smilemakers.data.network.response.LoginResponse


class UserRepository(
    private val myApi: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun userLogin(mobile: String, password: String,user_type:String): LoginResponse {
        return apiRequest { myApi.userLogin(mobile, password,user_type) }
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()

}