package com.smilemakers.network.response

import com.smilemakers.db.entities.User


class LoginResponse(
    val isSuccessful: Boolean?,
    val message: String?,
    val user: User?
)
