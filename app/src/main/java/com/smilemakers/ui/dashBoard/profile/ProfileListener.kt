package com.smilemakers.ui.dashBoard.profile

import com.smilemakers.data.db.entities.DashBoard

interface ProfileListener {
    fun onStarted()
    fun onSuccess(profile: Profile)
    fun onFailure(message: String)
}
