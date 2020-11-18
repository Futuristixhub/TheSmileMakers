package com.smilemakers.ui.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ForgotPasswordViewModelFactory (
    private val repository: ForgorPasswordRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForgotPasswordVM(repository) as T
    }
}