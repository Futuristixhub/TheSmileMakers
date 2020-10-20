package com.smilemakers.dashBoard.doctorFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.appointmentFragment.AppointmentFragment
import com.smilemakers.dashBoard.profile.ProfileRepository
import com.smilemakers.dashBoard.profile.ProfileVM

class DoctorVieModelFactory(
                            private val repository: DoctorRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DoctorFragmentVM(repository) as T
    }
}