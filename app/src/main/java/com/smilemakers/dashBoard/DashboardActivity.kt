package com.smilemakers.dashBoard

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.smilemakers.R
import com.smilemakers.dashBoard.appointmentFragment.AppointmentFragment
import com.smilemakers.dashBoard.dashBoardFragment.DashboardFragment
import com.smilemakers.dashBoard.doctorFragment.DoctorFragment
import com.smilemakers.dashBoard.patientFragment.PatientFragment
import com.smilemakers.dashBoard.profile.ProfileFragment
import com.smilemakers.databinding.ActivityDashboardBinding
import com.smilemakers.login.LoginActivity
import com.smilemakers.utils.color
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity() {

    var binding: ActivityDashboardBinding? = null
    val dashboardVM = DashboardVM(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setUpBinding()

        btm_navigation.itemBackgroundResource = R.drawable.bottom_nav_background_state
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked), // checked
            intArrayOf(-android.R.attr.state_checked) // unchecked
        )

        val colors = intArrayOf(
            R.color.colorAccent.color(this),
            R.color.white.color(this)
        )

        btm_navigation.itemIconTintList = ColorStateList(states, colors)
        btm_navigation.itemTextColor = ColorStateList(states, colors)
//        btm_navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        btm_navigation.setOnNavigationItemSelectedListener {
            title = it.title
            when (it.itemId) {
                R.id.btm_nav_action_dashboard -> callFragment(DashboardFragment.newInstance(this@DashboardActivity))
                R.id.btm_nav_action_paitent -> callFragment(PatientFragment.newInstance(this@DashboardActivity))
                R.id.btm_nav_action_doctor -> callFragment(DoctorFragment.newInstance(this@DashboardActivity))
                R.id.btm_nav_action_appointment -> callFragment(AppointmentFragment.newInstance(this@DashboardActivity))
                R.id.btm_nav_action_settings -> callFragment(ProfileFragment.newInstance(this@DashboardActivity))
            }
            true
        }
//        btm_navigation.selectedItemId = R.id.btm_nav_action_dashboard
        title = "Dashboard"
        callFragment(DashboardFragment.newInstance(this@DashboardActivity))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                val loginIntent = Intent(this, LoginActivity::class.java)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(loginIntent)
                finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1223) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.i("DashBoard TAG", "Place: " + place.name + ", " + place.id)
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) { // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("DashBoard TAG", status.getStatusMessage())
            } else if (resultCode === Activity.RESULT_CANCELED) { // The user canceled the operation.
            }
        }
    }

    private fun callFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_dash_container,fragment!!)
        transaction.commit()
    }

    private fun setUpBinding() {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_dashboard,null, false)
        binding?.vm = dashboardVM
    }
}
