package com.smilemakers.dashBoard.doctorFragment.addDoctor

import android.view.View
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.doctorFragment.doctorAddress.DoctorAddressFragment
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment
import com.smilemakers.dashBoard.patientFragment.patientAddress.PatientAddressFragment

class AddDoctorFragmentVM(val mFragment: AddDoctorFragment, val mActivity: DashboardActivity) : ViewModel() {
    fun onAddressClick(view: View) {
        val transaction = mActivity.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(R.id.fl_dash_container, DoctorAddressFragment.newInstance(mActivity)!!)
        transaction.commit()
    }
}