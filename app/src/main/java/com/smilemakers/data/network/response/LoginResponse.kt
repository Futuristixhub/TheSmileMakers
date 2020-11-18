package com.smilemakers.data.network.response

import com.smilemakers.data.db.entities.User


class LoginResponse(
    val status: Boolean?,
    val message: String?,
    val data: User?
)
