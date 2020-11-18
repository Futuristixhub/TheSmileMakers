package com.smilemakers.ui.forgotPassword

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.ui.login.LoginActivity
import com.smilemakers.utils.hideKeyboard
import com.smilemakers.utils.showErrorSnackBar

class ForgotPasswordVM(val repository: ForgorPasswordRepository) : ViewModel() {

    val mobileNumber = MutableLiveData<String>()
    val otp = MutableLiveData<String>()
    val npwd = MutableLiveData<String>()
    val cpwd = MutableLiveData<String>()

    fun onSendClick(view: View) {
        view.context.hideKeyboard(view)
        if (isValid(view)) {
            view.context.startActivity(Intent(view.context, ResetPasswordActivity::class.java))
        }
    }

    fun isValid(view: View): Boolean {

        if (mobileNumber.value == null || mobileNumber.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_mob_no))
            return false
        }
        if (mobileNumber.value?.length!! < 10) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.valid_mob_no))
            return false
        }

        return true
    }


    fun onSubmitClick(view: View) {
        view.context.hideKeyboard(view)
        if (isPwdValid(view)) {
            view.context.startActivity(
                Intent(view.context, LoginActivity::class.java).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            )
        }
    }

    private fun isPwdValid(view: View): Boolean {
        if (otp.value == null || otp.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_otp))
            return false
        }
        if (npwd.value == null || npwd.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_npwd))
            return false
        }
        if (cpwd.value == null || cpwd.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_cfpwd))
            return false
        }
        if (npwd.value != cpwd.value) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.pwd_match_error))
            return false
        }
        return true
    }
}