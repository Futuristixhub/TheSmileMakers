package com.smilemakers.dashBoard.patientFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smilemakers.dashBoard.DashboardActivity

class PatientViewModelFactory(
    private val repository: PatientRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PatientFragmentVM( repository) as T
    }
}