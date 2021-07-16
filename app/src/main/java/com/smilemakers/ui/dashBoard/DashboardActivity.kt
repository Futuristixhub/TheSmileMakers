package com.smilemakers.ui.dashBoard

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.simplemobiletools.calendar.pro.fragments.DayFragmentsHolder
import com.smilemakers.R
import com.smilemakers.databinding.ActivityDashboardBinding
import com.smilemakers.ui.dashBoard.appointmentFragment.AppointmentFragment
import com.smilemakers.ui.dashBoard.appointmentFragment.calendar.SimpleActivity
import com.smilemakers.ui.dashBoard.dashBoardFragment.DashboardFragment
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorFragment
import com.smilemakers.ui.dashBoard.patientFragment.PatientFragment
import com.smilemakers.ui.dashBoard.profile.ProfileFragment
import com.smilemakers.ui.login.LoginActivity
import com.smilemakers.utils.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.joda.time.DateTime
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein


class DashboardActivity : SimpleActivity(), KodeinAware {

    override val kodein by kodein()
    var binding: ActivityDashboardBinding? = null
    var fragment: Fragment? = null
    val dashboardVM = DashboardVM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val bar: ActionBar? = supportActionBar
        if (bar != null) {
            val tv = TextView(applicationContext)
            val lp: ActionBar.LayoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,  //Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT
            ) //Height of TextView
            tv.layoutParams = lp
            tv.text = getString(R.string.title_dashboard)
            tv.setTextColor(Color.WHITE)

            val typedValue = TypedValue()
            resources.getValue(R.dimen.actionBar_text, typedValue, true)
            val myFloatValue = typedValue.float

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, myFloatValue)
            bar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            bar.customView = tv

        }

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
        val user_type = getData(
            this,
            getString(R.string.user_type)
        )
        if ("admin".equals(user_type, true))
            btm_navigation.menu.removeItem(R.id.btm_nav_action_settings)
else    if ("patient".equals(user_type, true)) {
            btm_navigation.menu.removeItem(R.id.btm_nav_action_paitent)
            btm_navigation.menu.removeItem(R.id.btm_nav_action_doctor)
        }
        btm_navigation.setOnNavigationItemSelectedListener {
            title = it.title
            when (it.itemId) {
                R.id.btm_nav_action_dashboard -> callFragment(DashboardFragment.newInstance(this@DashboardActivity))
                R.id.btm_nav_action_paitent -> callFragment(PatientFragment.newInstance())
                R.id.btm_nav_action_doctor -> callFragment(DoctorFragment.newInstance())
                R.id.btm_nav_action_appointment -> {
                    val dayCode = intent.getStringExtra(DAY_CODE) ?: ""
                    val bundle = Bundle()
                    bundle.putString(DAY_CODE, dayCode)
                    var fragment1 = AppointmentFragment.newInstance(this@DashboardActivity)
                    fragment1!!.arguments = bundle
                    callFragment(fragment1)
                }
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
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.app_name)
                builder.setIcon(R.mipmap.logo)
                builder.setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialog, id ->
                            saveData(this, getString(R.string.is_logged_in), "false")
                            val loginIntent = Intent(this, LoginActivity::class.java)
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(loginIntent)
                            finish()
                        })
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                val alert: AlertDialog = builder.create()
                alert.show()

            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1223) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.i("DashBoard TAG", "Place: " + place.name + ", " + place.id)
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
            } else if (resultCode === Activity.RESULT_CANCELED) { // The user canceled the operation.
            }
        }
    }

    private fun callFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_dash_container, fragment!!)
        transaction.commit()
    }

    private fun setUpBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding?.vm = dashboardVM
    }

    override fun onBackPressed() {
        if (fragment != null) {
            removeTopFragment()
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name)
            builder.setIcon(R.mipmap.logo)
            builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id -> finish() })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
    }

    private fun removeTopFragment() {
        this.fragment?.let { supportFragmentManager.beginTransaction().remove(it).commit() }
        //fragment.removeAt(currentFragments.size - 1)
        //   calendar_fab.beGoneIf(currentFragments.size == 1 && config.storedView == YEARLY_VIEW)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun openDayFromMonthly(dateTime: DateTime?) {

        fragment = DayFragmentsHolder()
        val bundle = Bundle()
        bundle.putString(DAY_CODE, dateTime?.let { Formatter.getDayCodeFromDateTime(it) })
        (fragment as DayFragmentsHolder).arguments = bundle
        try {
            supportFragmentManager.beginTransaction().add(
                R.id.fl_dash_container,
                fragment as DayFragmentsHolder
            )
                .commitNow()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } catch (e: Exception) {
        }

    }

}
