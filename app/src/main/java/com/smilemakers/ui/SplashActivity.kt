package com.smilemakers.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.DashboardActivity
import com.smilemakers.ui.login.LoginActivity
import com.smilemakers.utils.getData
import com.smilemakers.utils.saveData
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
        iv_logo.startAnimation(slideAnimation)

        val splash_status = getData(this, getString(R.string.splash_status))

        if (splash_status.isNullOrEmpty()) {
            Handler().postDelayed({
                saveData(this, getString(R.string.splash_status), "true")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 2000)
        }else{
            val is_logged_in = getData(this, getString(R.string.is_logged_in))

            if (is_logged_in.equals("true")) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}
