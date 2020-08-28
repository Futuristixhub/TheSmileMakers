package com.smilemakers.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.smilemakers.R
import com.smilemakers.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var loginBinding: ActivityLoginBinding? = null
    var loginVM = LoginVM(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpBinding()
    }

    private fun setUpBinding() {
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginBinding?.vm = loginVM
    }
}
