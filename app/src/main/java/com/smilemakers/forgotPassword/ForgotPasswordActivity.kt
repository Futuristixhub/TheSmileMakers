package com.smilemakers.forgotPassword

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.smilemakers.R
import com.smilemakers.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    var binding : ActivityForgotPasswordBinding? = null
    val forgotPasswordVM = ForgotPasswordVM(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setUpBinding()
    }

    private fun setUpBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding?.vm = forgotPasswordVM
    }
}
