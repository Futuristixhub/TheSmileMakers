package com.smilemakers.dashBoard.doctorFragment.addDoctor

import android.view.View
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.doctorFragment.detail.DetailFragment

class AddDoctorFragmentVM(val mFragment: AddDoctorFragment, val mActivity: DashboardActivity) : ViewModel() {
    fun onNextClick(view: View) {
        val transaction = mActivity.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fl_dash_container, DetailFragment.newInstance(mActivity)!!)
        transaction.commit()
    }
}