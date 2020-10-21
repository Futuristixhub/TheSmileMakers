package com.smilemakers.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.databinding.ActivityLoginBinding
import com.smilemakers.db.entities.User
import com.smilemakers.utils.hide
import com.smilemakers.utils.show
import com.smilemakers.utils.showErrorSnackBar
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity() , AuthListener, KodeinAware {

    override val kodein by kodein()
    var loginBinding: ActivityLoginBinding? = null
    private val factory: AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpBinding()
    }

    private fun setUpBinding() {
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val  viewModel =
            ViewModelProviders.of(this, factory).get(LoginVM::class.java)
        loginBinding?.vm = viewModel
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        showErrorSnackBar(root_layout,message)
    }
}
