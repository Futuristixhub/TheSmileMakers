package com.smilemakers.login

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.forgotPassword.ForgotPasswordActivity
import com.smilemakers.utils.color

class LoginVM(private val repository: UserRepository) : ViewModel() {

    val mobileNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    var authListener: AuthListener? = null

    fun getloggedInUser() = repository.getUser()

    fun onForgotPasswordClick(view: View) {
        view.context.startActivity(Intent(view.context, ForgotPasswordActivity::class.java))
    }

    fun onLoginClick(view: View) {
      //  if (isValid(view)) {
            view.context.startActivity(Intent(view.context, DashboardActivity::class.java))
        //}

        /*  authListener?.onStarted()
       if (mobile.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("mobile or Password is Empty")
            return
        }

        Coroutines.main {
            try {
                val authResponse = repository.userLogin(mobile!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e: ApiExceptions) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }*/
    }

    fun isValid(view: View): Boolean {

        if (mobileNumber.value == null || mobileNumber.value?.isEmpty()!!) {
            showErrorSnackBar(view,"Please enter mobile number", "OK")
            return false
        }
        if (mobileNumber.value?.length!! < 10) {
            showErrorSnackBar(view,"Please enter valid mobile number", "OK")
            return false
        }
        if (password.value == null || password.value?.isEmpty()!!) {
            showErrorSnackBar(view,"Please enter password", "OK")
            return false
        }
        if (password.value?.length!! < 4) {
            showErrorSnackBar(view,"Password must have at least 4 characters", "OK")
            return false
        }
        return true
    }

    fun showErrorSnackBar(view: View, errorMsg: String, actionBtn: String = "") {
        val errorSnackbar =
            Snackbar.make(view, errorMsg, Snackbar.LENGTH_LONG)
        if (actionBtn.isNotEmpty()) {
            errorSnackbar.setAction(actionBtn) {
                if (errorSnackbar.isShown)
                    errorSnackbar.dismiss()
            }
            errorSnackbar.setActionTextColor(R.color.white.color(view.context))
        }
        errorSnackbar.show()
    }
}