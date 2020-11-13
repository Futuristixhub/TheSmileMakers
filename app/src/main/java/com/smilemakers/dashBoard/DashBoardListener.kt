package com.smilemakers.dashBoard

import com.smilemakers.db.entities.DashBoard

interface DashBoardListener {
    fun onStarted()
    fun onSuccess(dashBoard: DashBoard)
    fun onFailure(message: String)
}