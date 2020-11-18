package com.smilemakers.ui.dashBoard.doctorFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DoctorVieModelFactory(
                            private val repository: DoctorRepository,val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DoctorFragmentVM(repository,application) as T
    }
}