package com.smilemakers.ui.dashBoard

import com.smilemakers.data.db.entities.DashBoard

interface DashBoardListener {
    fun onStarted()
    fun onSuccess(dashBoard: DashBoard)
    fun onFailure(message: String)
}