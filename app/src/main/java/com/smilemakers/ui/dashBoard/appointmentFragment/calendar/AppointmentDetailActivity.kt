package com.smilemakers.ui.dashBoard.appointmentFragment.calendar

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.appointmentFragment.AppointMentViemodelFactory
import com.smilemakers.ui.dashBoard.appointmentFragment.AppointmentFragmentVM
import com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment.Event
import com.smilemakers.databinding.ActivityAppointmentDetailBinding
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.utils.*
import com.smilemakers.utils.Formatter
import kotlinx.android.synthetic.main.activity_appointment_detail.*
import org.joda.time.DateTime
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class AppointmentDetailActivity : AppCompatActivity(), KodeinAware, PatientListener {

    override val kodein by kodein()
    var binding: ActivityAppointmentDetailBinding? = null
    private val factory: AppointMentViemodelFactory by instance()
    var viewModel: AppointmentFragmentVM? = null

    private var mStoredEventTypes = ArrayList<EventType>()
    private var mEventTypeId = REGULAR_EVENT_TYPE_ID
    private var mEventOccurrenceTS = 0L
    private lateinit var mEvent: Event
    private lateinit var mEventStartDateTime: DateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_detail)
        viewModel =
            ViewModelProviders.of(this, factory).get(AppointmentFragmentVM::class.java)
        binding?.vm = viewModel
        viewModel?.authListener = this

        val bar: ActionBar? = supportActionBar
        if (bar != null) {
            val tv = TextView(applicationContext)
            val lp: ActionBar.LayoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,  // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT
            ) // Height of TextView
            tv.layoutParams = lp
            tv.setText(getString(R.string.patient_detail))
            tv.setTextColor(Color.WHITE)

            val typedValue = TypedValue()
            resources.getValue(R.dimen.actionBar_text, typedValue, true)
            val myFloatValue = typedValue.float

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, myFloatValue)
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
            bar.setDisplayHomeAsUpEnabled(true)
            bar.setCustomView(tv)
        }

        if(getData(this, getString(R.string.user_type))!!.toLowerCase(Locale.ROOT).equals("patient")){
            binding?.edPresc?.isEnabled = false
            binding?.btnSubmit?.visibility = View.GONE
        }else{
            binding?.edPresc?.isEnabled = true
            binding?.btnSubmit?.visibility = View.VISIBLE
        }

        setEventData(savedInstanceState)

    }

    private fun setEventData(savedInstanceState: Bundle?) {
        val eventId = intent.getLongExtra(EVENT_ID, 0L)
        ensureBackgroundThread {
            mStoredEventTypes = eventTypesDB.getEventTypes().toMutableList() as ArrayList<EventType>
            val event = eventsDB.getEventWithId(eventId)
            if (eventId != 0L && event == null) {
                finish()
                return@ensureBackgroundThread
            }

            val localEventType =
                mStoredEventTypes.firstOrNull { it.id == config.lastUsedLocalEventTypeId }
            runOnUiThread {
                gotEvent(savedInstanceState, localEventType, event)
            }
        }
    }

    private fun gotEvent(savedInstanceState: Bundle?, localEventType: EventType?, event: Event?) {
        if (localEventType == null || localEventType.caldavCalendarId != 0) {
            config.lastUsedLocalEventTypeId = REGULAR_EVENT_TYPE_ID
        }


        mEventTypeId =
            if (config.defaultEventTypeId == -1L) config.lastUsedLocalEventTypeId else config.defaultEventTypeId

        if (event != null) {
            mEvent = event
            mEventOccurrenceTS = intent.getLongExtra(EVENT_OCCURRENCE_TS, 0L)
            if (savedInstanceState == null) {
                mEventStartDateTime = Formatter.getDateTimeFromTS(mEventOccurrenceTS)
                viewModel?.app_id = mEvent.ap_id
                tv_pname.text = mEvent.title
                tv__apt_date.text = Formatter.getDate(applicationContext, mEventStartDateTime)
                tv_apt_time.text = Formatter.getTime(this, mEventStartDateTime)
                tv_location.text = mEvent.location
                tv_trmt_type.text = mEvent.treatment_type
                tv_dr_name.text = mEvent.title
                if(!mEvent.prescription.isNullOrEmpty()){
                    ed_presc.setText(mEvent.prescription)
                }
            }

            if (intent.getBooleanExtra(IS_DUPLICATE_INTENT, false)) {
                mEvent.id = null
            } else {
                cancelNotification(mEvent.id!!)
            }
        } else {
            mEvent =
                Event(
                    null
                )

        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onStarted() {
        binding?.progressBar?.show()
    }

    override fun onSuccess(message: String) {
        binding?.progressBar?.hide()
        showErrorSnackBar(root_layout, message)
    }

    override fun onFailure(message: String) {
        binding?.progressBar?.hide()
        showErrorSnackBar(root_layout, message)
    }
}
