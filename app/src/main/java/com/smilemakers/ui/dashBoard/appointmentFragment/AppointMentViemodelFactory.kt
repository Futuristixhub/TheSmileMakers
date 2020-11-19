package com.smilemakers.ui.dashBoard.appointmentFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppointMentViemodelFactory(
    private val repository: AppointmentRepository, val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppointmentFragmentVM(repository, application) as T
    }
}