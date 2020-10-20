package com.smilemakers.dashBoard.doctorFragment.addDoctor

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.doctorFragment.detail.DetailFragment

class AddDoctorFragmentVM() : ViewModel() {
    fun onNextClick(view: View) {
        val transaction = (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fl_dash_container, DetailFragment.newInstance()!!)
        transaction.commit()
    }
}