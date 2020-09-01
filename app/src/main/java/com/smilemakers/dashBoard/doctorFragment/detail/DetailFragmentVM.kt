package com.smilemakers.dashBoard.doctorFragment.detail

import android.view.View
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.doctorFragment.doctorAddress.DoctorAddressFragment

class DetailFragmentVM(val mFragment: DetailFragment, val mActivity: DashboardActivity) : ViewModel() {
    fun onAddressClick(view: View) {
        val transaction = mActivity.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(R.id.fl_dash_container, DoctorAddressFragment.newInstance(mActivity)!!)
        transaction.commit()
    }
}