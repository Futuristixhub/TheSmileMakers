package com.smilemakers.dashBoard.doctorFragment.addDoctor

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.doctorFragment.detail.DetailFragment
import com.smilemakers.utils.hideKeyboard
import com.smilemakers.utils.showErrorSnackBar

class AddDoctorFragmentVM() : ViewModel() {

    val fname = MutableLiveData<String>()
    val lname = MutableLiveData<String>()
    val dob = MutableLiveData<String>()
    val age = MutableLiveData<String>()
    val education = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val mNumber = MutableLiveData<String>()

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun onNextClick(view: View) {
        view.context.hideKeyboard(view)
        //  if (isValid(view)) {
        val transaction =
            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
        //  transaction.addToBackStack(null)
        transaction.replace(R.id.fl_container, DetailFragment.newInstance()!!)
        transaction.commit()
        //}
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
        if (education.value == null || education.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_education))
            return false
        }
        if (email.value == null || email.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_email))
            return false
        }
       if (mNumber.value == null || mNumber.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_mob_no))
            return false
        }
        if(!email.value!!.matches(emailPattern.toRegex())){
            view.context.showErrorSnackBar(view, view.context.getString(R.string.valid_email))
        }
        if (mNumber.value?.length!! < 10) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.valid_mob_no))
            return false
        }

        return true
    }
}