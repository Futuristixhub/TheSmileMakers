package com.smilemakers.dashBoard.patientFragment.addPatient

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.patientFragment.patientAddress.PatientAddressFragment
import com.smilemakers.utils.hideKeyboard
import com.smilemakers.utils.showErrorSnackBar

class AddPatientFragmentVM() : ViewModel() {

    val fname = MutableLiveData<String>()
    val lname = MutableLiveData<String>()
    val dob = MutableLiveData<String>()
    val age = MutableLiveData<String>()
    val refId = MutableLiveData<String>()
    val refName = MutableLiveData<String>()
    val mNumber = MutableLiveData<String>()

    fun onNextClick(view: View) {
        view.context.hideKeyboard(view)
        //if (isValid(view)) {
            val transaction =
                (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            //  transaction.addToBackStack(null)
            transaction.replace(R.id.fl_container, PatientAddressFragment.newInstance()!!)
            transaction.commit()
       // }
    }

    fun isValid(view: View): Boolean {

        if (fname.value == null || fname.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_fname))
            return false
        }
        if (lname.value == null || lname.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_lname))
            return false
        }
        if (dob.value == null || dob.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_dob))
            return false
        }
        if (age.value == null || age.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_age))
            return false
        }
        if (refId.value == null || refId.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_ref_id))
            return false
        }
        if (refName.value == null || refName.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_ref_name))
            return false
        }
        if (mNumber.value == null || mNumber.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_mob_no))
            return false
        }
        if (mNumber.value?.length!! < 10) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.valid_mob_no))
            return false
        }

        return true
    }
}