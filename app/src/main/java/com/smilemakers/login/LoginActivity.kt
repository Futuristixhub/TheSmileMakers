package com.smilemakers.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.simplemobiletools.commons.extensions.toast
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.ActivityLoginBinding
import com.smilemakers.db.entities.User
import com.smilemakers.utils.hide
import com.smilemakers.utils.saveData
import com.smilemakers.utils.show
import com.smilemakers.utils.showErrorSnackBar
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), AuthListener, KodeinAware {

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
        val viewModel =
            ViewModelProviders.of(this, factory).get(LoginVM::class.java)
        loginBinding?.vm = viewModel
        viewModel.authListener = this
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
        saveData(this, getString(R.string.is_logged_in), "true")
        saveData(this, getString(R.string.user_id), user.pid!!)
        startActivity(
            Intent(this, DashboardActivity::class.java).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
    }

    override fun onFailure(message: String) {
        progress_bar.hide()

        showErrorSnackBar(root_layout, message)
    }

}
