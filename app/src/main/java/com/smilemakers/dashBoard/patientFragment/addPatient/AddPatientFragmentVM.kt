package com.smilemakers.dashBoard.patientFragment.addPatient

import android.view.View
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.patientFragment.patientAddress.PatientAddressFragment

class AddPatientFragmentVM(val mFragment: AddPatientFragment, val mActivity: DashboardActivity) : ViewModel() {
    fun onAddressClick(view: View) {
        val transaction = mActivity.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(R.id.fl_dash_container, PatientAddressFragment.newInstance(mActivity)!!)
        transaction.commit()
    }
}