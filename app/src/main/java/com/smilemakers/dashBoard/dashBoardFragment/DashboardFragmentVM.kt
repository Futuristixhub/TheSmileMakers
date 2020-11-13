package com.smilemakers.dashBoard.dashBoardFragment

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashBoardListener
import com.smilemakers.dashBoard.DashBoardRepository
import com.smilemakers.dashBoard.doctorFragment.DoctorFragment
import com.smilemakers.dashBoard.patientFragment.PatientFragment
import com.smilemakers.db.entities.DashBoard
import com.smilemakers.utils.ApiExceptions
import com.smilemakers.utils.Coroutines
import com.smilemakers.utils.NoInternetException

class DashboardFragmentVM(private val repository: DashBoardRepository) : ViewModel() {

    var dashboardData = MutableLiveData<DashBoard>()

    fun onPatientsClick(view: View) {
        val transaction =
            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(R.id.fl_dash_container, PatientFragment.newInstance()!!)
        transaction.commit()
        //mActivity.btm_navigation.selectedItemId = R.id.btm_nav_action_paitent
    }

    fun onDoctorsClick(view: View) {
        val transaction =
            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(R.id.fl_dash_container, DoctorFragment.newInstance()!!)
        transaction.commit()
    }

    var dashBoardListener: DashBoardListener? = null

    fun getDashBoardData(uid: String?) {
        dashBoardListener!!.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.dashBoardCount(uid!!)
                authResponse.data?.let {
                    dashBoardListener?.onSuccess(it)
                    dashboardData.value = it
                    return@main
                }
                dashBoardListener?.onFailure(authResponse.message!!)
            } catch (e: ApiExceptions) {
                dashBoardListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                dashBoardListener?.onFailure(e.message!!)
            }
        }
    }
}