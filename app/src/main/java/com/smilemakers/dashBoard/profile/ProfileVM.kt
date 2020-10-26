package com.smilemakers.dashBoard.profile

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.utils.hideKeyboard
import com.smilemakers.utils.showErrorSnackBar

class ProfileVM(val repository: ProfileRepository) : ViewModel() {

    var name: String? = null
    var subname: String? = null
    var mob_no: String? = null
    var email: String? = null
    var location: String? = null

    var edt_fname: String? = null
    var edt_lname: String? = null
    var edt_subname: String? = null
    var edt_mob_no: String? = null
    var edt_email: String? = null
    var edt_location: String? = null

    var edt_cpwd: String? = null
    var edt_npwd: String? = null
    var edt_cfpwd: String? = null

    init {
        name = "Mansi Bhatt"
        subname = "Android Developer"
        mob_no = "+91 1234567890"
        email = "test@gmail.com"
        location = "Bapunagar"

        edt_fname = "Mansi"
        edt_lname = "Bhatt"
        edt_subname = "Android Developer"
        edt_mob_no = "+91 1234567890"
        edt_email = "test@gmail.com"
        edt_location = "Bapunagar"
    }

    fun onEditClick(view: View) {
        view.context.startActivity(Intent(view.context, EditProfileActivity::class.java))
    }

    fun onChangePwdClick(view: View) {
        view.context.startActivity(Intent(view.context, ChangePasswordActivity::class.java))
    }

    fun onSubmitClick(view: View) {
        view.context.hideKeyboard(view)
        if (isValid(view)) {
            (view.context as AppCompatActivity).finish()
        }
    }

    fun isValid(view: View): Boolean {

        if (edt_fname == null || edt_fname!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_fname))
            return false
        }
        if (edt_lname == null || edt_lname!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_lname))
            return false
        }
        if (edt_subname == null || edt_subname!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_bio))
            return false
        }
        if (edt_location == null || edt_location!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_location))
            return false
        }
        if (edt_email == null || edt_email!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_email))
            return false
        }
        if (edt_mob_no == null || edt_mob_no!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_mob_no))
            return false
        }

        return true
    }

    fun onCPSubmitClick(view: View) {
        view.context.hideKeyboard(view)
        if (isCPValid(view)) {
            (view.context as AppCompatActivity).finish()
        }
    }

    fun isCPValid(view: View): Boolean {

        if (edt_cpwd == null || edt_cpwd!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_cpwd))
            return false
        }
        if (edt_npwd == null || edt_npwd!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_npwd))
            return false
        }
        if (edt_cfpwd == null || edt_cfpwd!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_cfpwd))
            return false
        }
        if(!edt_npwd.equals(edt_cfpwd)){
            view.context.showErrorSnackBar(view, view.context.getString(R.string.pwd_match_error))
            return false
        }

        return true
    }
}