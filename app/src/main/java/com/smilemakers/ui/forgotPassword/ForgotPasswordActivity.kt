package com.smilemakers.ui.forgotPassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.databinding.ActivityForgotPasswordBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ForgotPasswordActivity : AppCompatActivity() , KodeinAware {

    override val kodein by kodein()

    private val factory: ForgotPasswordViewModelFactory by instance()

    var binding : ActivityForgotPasswordBinding? = null

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setUpBinding()
    }

    private fun setUpBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        val  viewModel =
            ViewModelProviders.of(this, factory).get(ForgotPasswordVM::class.java)
        binding?.vm = viewModel
    }
}
