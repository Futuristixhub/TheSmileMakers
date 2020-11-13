package com.smilemakers.network.response

import com.smilemakers.db.entities.User


class LoginResponse(
    val status: Boolean?,
    val message: String?,
    val data: User?
)
