package com.smilemakers.ui.dashBoard.patientFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PatientViewModelFactory(
    private val repository: PatientRepository,val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PatientFragmentVM( repository,application) as T
    }
}