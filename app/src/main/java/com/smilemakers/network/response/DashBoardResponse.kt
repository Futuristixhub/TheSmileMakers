package com.smilemakers.network.response

import com.smilemakers.db.entities.DashBoard
import com.smilemakers.db.entities.User

class DashBoardResponse(
    val status: Boolean?,
    val message: String?,
    val data: DashBoard?
)
