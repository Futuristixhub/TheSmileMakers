package com.smilemakers.data.network.response

import com.smilemakers.data.db.entities.DashBoard

class DashBoardResponse(
    val status: Boolean?,
    val message: String?,
    val data: DashBoard?
)
