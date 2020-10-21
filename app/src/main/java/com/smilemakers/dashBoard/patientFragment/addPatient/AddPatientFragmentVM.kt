package com.smilemakers.dashBoard.patientFragment.addPatient

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.patientFragment.patientAddress.PatientAddressFragment

class AddPatientFragmentVM() : ViewModel() {
    fun onAddressClick(view: View) {
        val transaction = (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
      //  transaction.addToBackStack(null)
        transaction.replace(R.id.fl_container, PatientAddressFragment.newInstance()!!)
        transaction.commit()
    }
}