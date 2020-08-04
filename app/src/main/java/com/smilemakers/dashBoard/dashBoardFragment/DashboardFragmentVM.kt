package com.smilemakers.dashBoard.dashBoardFragment

import android.view.View
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.patientFragment.PatientFragment
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardFragmentVM(mFragment: DashboardFragment, val mActivity: DashboardActivity) : ViewModel() {
    fun onPatientsClick(view: View) {
//        val transaction = mActivity.supportFragmentManager.beginTransaction()
//        transaction.addToBackStack(null)
//        transaction.add(R.id.fl_dash_container, PatientFragment.newInstance(mActivity)!!)
//        transaction.commit()
        mActivity.btm_navigation.selectedItemId = R.id.btm_nav_action_paitent
    }
}