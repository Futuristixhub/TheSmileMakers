package com.smilemakers.login

import android.content.Intent
import android.text.Editable
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.forgotPassword.ForgotPasswordActivity
import com.smilemakers.utils.color
import kotlinx.android.synthetic.main.activity_login.*

class LoginVM(val mActivity: LoginActivity) : ViewModel() {

    val mobileNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun onForgotPasswordClick(view: View) {
        mActivity.startActivity(Intent(mActivity, ForgotPasswordActivity::class.java))
    }

    fun onLoginClick(view: View) {
        if (isValid()) {
            mActivity.startActivity(Intent(mActivity, DashboardActivity::class.java))
        }
    }

    fun isValid(): Boolean {
        if (mobileNumber.value == null || mobileNumber.value?.isEmpty()!!) {
            showErrorSnackBar("Please enter mobile number", "OK")
            return false
        }
        if (mobileNumber.value?.length!! < 10) {
            showErrorSnackBar("Please enter valid mobile number", "OK")
            return false
        }
        if (password.value == null || password.value?.isEmpty()!!) {
            showErrorSnackBar("Please enter password", "OK")
            return false
        }
        if (password.value?.length!! < 4) {
            showErrorSnackBar("Password must have at least 4 characters", "OK")
            return false
        }
        return true
    }

    fun showErrorSnackBar(errorMsg: String, actionBtn: String = "") {
        val errorSnackbar =
            Snackbar.make(mActivity.loginBinding?.root!!, errorMsg, Snackbar.LENGTH_LONG)
        if (actionBtn.isNotEmpty()) {
            errorSnackbar.setAction(actionBtn) {
                if (errorSnackbar.isShown)
                    errorSnackbar.dismiss()
            }
            errorSnackbar.setActionTextColor(R.color.white.color(mActivity))
        }
        errorSnackbar.show()
    }
}