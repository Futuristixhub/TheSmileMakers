package com.smilemakers.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.DashboardActivity
import com.smilemakers.ui.login.LoginActivity
import com.smilemakers.utils.getData

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val is_logged_in = getData(this, getString(R.string.is_logged_in))
            if (is_logged_in.equals("true")) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }
}
