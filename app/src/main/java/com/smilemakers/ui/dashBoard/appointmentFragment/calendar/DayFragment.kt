package com.simplemobiletools.calendar.pro.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment.Event
import com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment.Events
import com.smilemakers.ui.dashBoard.appointmentFragment.calendar.AppointmentDetailActivity
import com.smilemakers.ui.dashBoard.appointmentFragment.calendar.DayEventsAdapter
import com.smilemakers.ui.dashBoard.appointmentFragment.calendar.NavigationListener
import com.smilemakers.ui.dashBoard.appointmentFragment.calendar.SimpleActivity
import com.smilemakers.ui.dashBoard.doctorFragment.addDoctor.AddDoctorFragment
import com.smilemakers.utils.*
import com.smilemakers.utils.Formatter
import kotlinx.android.synthetic.main.fragment_day.view.*
import kotlinx.android.synthetic.main.top_navigation.view.*
import java.util.*

class DayFragment : Fragment() {
    private var str = ""
    var mListener: NavigationListener? = null
    private var mDayCode = ""
    private var lastHash = 0

    private lateinit var mHolder: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        mHolder = view.day_holder

        mDayCode = arguments!!.getString(DAY_CODE).toString()
        str = resources.getString(R.string.bapunagar)
        setupButtons()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateCalendar()
    }

    private fun setupButtons() {
        //   mTextColor = context!!.config.textColor

        mHolder.top_left_arrow.apply {
            //    applyColorFilter(mTextColor-)
            background = null
            setOnClickListener {
                mListener?.goLeft()
            }

            val pointerLeft = context!!.getDrawable(R.drawable.ic_chevron_left_vector)
            pointerLeft?.isAutoMirrored = true
            setImageDrawable(pointerLeft)
        }

        mHolder.top_right_arrow.apply {
            //    applyColorFilter(mTextColor)
            background = null
            setOnClickListener {
                mListener?.goRight()
            }

            val pointerRight = context!!.getDrawable(R.drawable.ic_chevron_right_vector)
            pointerRight?.isAutoMirrored = true
            setImageDrawable(pointerRight)
        }
        //.20
        val day = Formatter.getDayTitle(context!!, mDayCode)
        mHolder.top_value.apply {//date
            text = day
            contentDescription = text
            setOnClickListener {
                // (activity as MainActivity).showGoToDateDialog()
            }
            setTextColor(context.config.textColor)
        }
    }

    fun updateCalendar() {
        val startTS = Formatter.getDayStartTS(mDayCode)
        val endTS = Formatter.getDayEndTS(mDayCode)
        context?.eventsHelper?.getEvents(startTS, endTS) {
            receivedEvents(it)
        }

    }

    private fun receivedEvents(events: List<Event>) {
        val newHash = events.hashCode()
        if (newHash == lastHash || !isAdded) {
            return
        }
        lastHash = newHash

        val replaceDescription = context!!.config.replaceDescription
        val sorted = ArrayList<Event>(
            events.sortedWith(
                compareBy({ !it.getIsAllDay() },
                    { it.startTS },
                    { it.endTS },
                    { it.title },
                    {
                        it.location
                    })
            )
        )

        activity?.runOnUiThread {
            updateEvents(sorted)
        }
    }

    private fun updateEvents(events: ArrayList<Event>) {
        val eventsnew = arrayListOf<Events>()
        if (activity == null)
            return
        val newList = arrayListOf<Event>()
        for (element in events) {
            if (!newList.contains(element)) {
                newList.add(element)
            }
        }
        for (i in newList.indices) {
            eventsnew.add(
                Events(
                    newList[i],
                    events
                )
            )
        }
        if (eventsnew.size > 0) {
            DayEventsAdapter(
                activity as SimpleActivity,
                eventsnew,
                mHolder.day_events
            ) {
                editEvent(it as Events)
            }.apply {
                mHolder.day_events.adapter = this
            }
            mHolder.top_header.visibility = View.VISIBLE
            mHolder.btm_header.visibility = View.VISIBLE
            mHolder.day_events.visibility = View.VISIBLE
            mHolder.tv_empty.visibility = View.GONE
        } else {
            mHolder.top_header.visibility = View.GONE
            mHolder.btm_header.visibility = View.GONE
            mHolder.day_events.visibility = View.GONE
            mHolder.tv_empty.visibility = View.VISIBLE
        }
    }

    private fun editEvent(event: Events) {

        Intent(context, AppointmentDetailActivity::class.java).apply {
            putExtra(EVENT_ID, event.event.id)
            putExtra(EVENT_OCCURRENCE_TS, event.event.startTS)
            startActivity(this)

        }
    }
}
