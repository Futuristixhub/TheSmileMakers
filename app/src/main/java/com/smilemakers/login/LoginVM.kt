package com.smilemakers.login

import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.forgotPassword.ForgotPasswordActivity
import com.smilemakers.utils.color
import com.smilemakers.utils.hideKeyboard
import com.smilemakers.utils.saveData
import com.smilemakers.utils.showErrorSnackBar

class LoginVM(private val repository: UserRepository) : ViewModel() {

    val mobileNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    var authListener: AuthListener? = null

    fun getloggedInUser() = repository.getUser()

    fun onForgotPasswordClick(view: View) {
        view.context.hideKeyboard(view)
        view.context.startActivity(Intent(view.context, ForgotPasswordActivity::class.java))
    }

    fun onLoginClick(view: View) {
        view.context.hideKeyboard(view)
        //  if (isValid(view)) {
        view.context.saveData(view.context, view.context.getString(R.string.is_logged_in), "true")
        view.context.startActivity(
            Intent(view.context, DashboardActivity::class.java).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
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
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_mob_no))
            return false
        }
        if (mobileNumber.value?.length!! < 10) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.valid_mob_no))
            return false
        }
        if (password.value == null || password.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_pwd))
            return false
        }
        if (password.value?.length!! < 4) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.valid_pwd))
            return false
        }
        return true
    }


}