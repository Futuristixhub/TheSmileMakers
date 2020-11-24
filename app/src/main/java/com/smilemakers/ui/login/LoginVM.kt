package com.smilemakers.ui.login

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.data.db.entities.User
import com.smilemakers.ui.forgotPassword.ForgotPasswordActivity
import com.smilemakers.utils.*

class LoginVM(private val repository: UserRepository) : ViewModel() {

    val mobileNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    var authListener: AuthListener? = null
    var user_type: String? = null

    fun getloggedInUser() = repository.getUser()
    suspend fun saveLoggedInUser(user: User) = repository.saveUser(user)

    fun onForgotPasswordClick(view: View) {
        view.context.hideKeyboard(view)
        view.context.startActivity(Intent(view.context, ForgotPasswordActivity::class.java))
    }

    fun onLoginClick(view: View) {
        view.context.hideKeyboard(view)
        if (isValid(view)) {
            authListener!!.onStarted()
            Coroutines.main {
                try {
                    val authResponse =
                        repository.userLogin(mobileNumber.value!!, password.value!!, user_type!!)
                    authResponse.data?.let {
                        if (authResponse.status == false) {
                            authListener?.onFailure(authResponse.message!!)
                        } else {
                            authListener?.onSuccess(it)
                            repository.saveUser(it)
                            return@main
                        }
                    }
                    authListener?.onFailure(authResponse.message!!)
                } catch (e: ApiExceptions) {
                    authListener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    authListener?.onFailure(e.message!!)
                }
            }
        }

    }

    fun isValid(view: View): Boolean {

        if (mobileNumber.value == null || mobileNumber.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(
                view,
                view.context.getString(R.string.empty_mob_no)
            )
            return false
        }
        if (mobileNumber.value?.length!! < 10) {
            view.context.showErrorSnackBar(
                view,
                view.context.getString(R.string.valid_mob_no)
            )
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