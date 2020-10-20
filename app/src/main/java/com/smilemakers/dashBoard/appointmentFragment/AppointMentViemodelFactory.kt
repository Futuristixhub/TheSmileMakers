package com.smilemakers.dashBoard.appointmentFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smilemakers.dashBoard.DashboardActivity

class AppointMentViemodelFactory(
                                 private val repository: AppointmentRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppointmentFragmentVM(repository) as T
    }
}