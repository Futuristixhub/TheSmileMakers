package com.smilemakers.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.DashboardActivity
import com.smilemakers.databinding.ActivityLoginBinding
import com.smilemakers.data.db.entities.User
import com.smilemakers.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class LoginActivity : AppCompatActivity(), AuthListener, KodeinAware , AdapterView.OnItemSelectedListener {

    override val kodein by kodein()
    var loginBinding: ActivityLoginBinding? = null
    lateinit var spinner: Spinner
    var viewModel :LoginVM?=null
    private val factory: AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpBinding()
    }

    private fun setUpBinding() {
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
         viewModel =
            ViewModelProviders.of(this, factory).get(LoginVM::class.java)
        loginBinding?.vm = viewModel
        viewModel?.authListener = this

        spinner = loginBinding?.root!!.findViewById(R.id.sp_user_type)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.user_type,
            R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this


    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
        saveData(this, getString(R.string.is_logged_in), "true")
        saveData(this, getString(R.string.user_id), user.user_id!!)
        saveData(this, getString(R.string.user_type), user.user_type!!.toLowerCase(Locale.ROOT))
        runOnUiThread {
            startActivity(
                Intent(this, DashboardActivity::class.java).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            )
        }
    }

    override fun onFailure(message: String) {
        progress_bar.hide()

        showErrorSnackBar(root_layout, message)
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        viewModel?.user_type = parent?.getItemAtPosition(position).toString().toLowerCase(Locale.ROOT)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}
