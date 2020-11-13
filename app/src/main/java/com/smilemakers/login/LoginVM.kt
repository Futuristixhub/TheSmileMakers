package com.smilemakers.login

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.db.entities.User
import com.smilemakers.forgotPassword.ForgotPasswordActivity
import com.smilemakers.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginVM(private val repository: UserRepository) : ViewModel() {

    val mobileNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    var authListener: AuthListener? = null
    val itemPosition = MutableLiveData<Int>()
    val items = arrayListOf("Doctor", "Patient", "Admin")

    // selected item
    private val selectItem get() = itemPosition.value?.let {
            items.get(it)
        }

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
                      val authResponse = repository.userLogin(mobileNumber.value!!, password.value!!,"patient")
                      authResponse.data?.let {
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