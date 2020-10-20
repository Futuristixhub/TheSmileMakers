package com.smilemakers.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smilemakers.dashBoard.profile.ProfileRepository
import com.smilemakers.dashBoard.profile.ProfileVM

class ForgotPasswordViewModelFactory (
    private val repository: ForgorPasswordRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForgotPasswordVM(repository) as T
    }
}