package com.smilemakers.ui.dashBoard.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileViewModelFactory (
    private val repository: ProfileRepository,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileVM(repository,application) as T
    }
}