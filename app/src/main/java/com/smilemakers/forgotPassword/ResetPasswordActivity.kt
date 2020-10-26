package com.smilemakers.forgotPassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.databinding.ActivityForgotPasswordBinding
import com.smilemakers.databinding.ActivityResetPasswordBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ResetPasswordActivity : AppCompatActivity() , KodeinAware {

    override val kodein by kodein()

    private val factory: ForgotPasswordViewModelFactory by instance()

    var binding : ActivityResetPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        setUpBinding()
    }

    private fun setUpBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        val  viewModel =
            ViewModelProviders.of(this, factory).get(ForgotPasswordVM::class.java)
        binding?.vm = viewModel
    }
}