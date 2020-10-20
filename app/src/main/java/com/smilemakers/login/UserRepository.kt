package com.smilemakers.login

import com.smilemakers.db.AppDatabase
import com.smilemakers.db.entities.User
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest
import com.smilemakers.network.response.LoginResponse


class UserRepository(
    private val myApi: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun userLogin(email: String, password: String): LoginResponse {
        return apiRequest { myApi.userLogin(email, password) }
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()

}