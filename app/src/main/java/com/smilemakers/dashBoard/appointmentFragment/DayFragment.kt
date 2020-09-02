package com.simplemobiletools.calendar.pro.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.simplemobiletools.commons.extensions.applyColorFilter
import com.smilemakers.R
import com.smilemakers.dashBoard.appointmentFragment.*
import com.smilemakers.utils.*
import com.smilemakers.utils.Formatter
import kotlinx.android.synthetic.main.event_item_day_view.view.*
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
        .20
        val day = Formatter.getDayTitle(context!!, mDayCode)
        mHolder.top_value.apply {
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
        var eventsnew = arrayListOf<Events>()
        if (activity == null)
            return
        Log.e("tagevejj", "event...." + events)
        var count = 0
        var title2 = ""
        var title3 = ""
        var title4 = ""

/*

        // for (i in events) {
        //    for (i in events.indices) {

//                if (i > 0) {
//                    if (events[i].equals(events[i - 1])) {
//                        count++
//                        if (count == 1) {
//                            title2 = events[i].title
//                            Log.e("taggggg","bbbb...2..."+events[i].title)
//
//                        } else
//                            if (count == 2) {
//                                title3 = events[i].title
//                                Log.e("taggggg","bbbb...3..."+events[i].title)
//                            } else
//                                if (count == 3) {
//                                    title4 = events[i].title
//                                    Log.e("taggggg","bbbb...4..."+events[i].title)
//                                }
//
//                        eventsnew.add(
//                            Events(
//                                events[i],
//                                title2,
//                                title3,
//                                title4
//                            )
//                        )
//                    } else {
//                        Log.e("taggggg","bbbb...1..."+events[i].title)
//                        eventsnew.add(
//                            Events(
//                                events.get(i),
//                                "",
//                                "", ""
//                            )
//                        )
//                    }
//                } else {
//                    Log.e("taggggg","bbbb......"+events[0].title)
//                    eventsnew.add(
//                        Events(
//                            events[0],
//                            "",
//                            "", ""
//                        )
//                    )
//                }
//            }
        //   }
*/
        /*   val newList = arrayListOf<Event>()
           val newList_b = arrayListOf<Event>()
           val newList_n = arrayListOf<Event>()
           for (element in events) {
               // then add it
               if (!newList.contains(element)) {
                   newList.add(element)
               }
           }

           for (i in events.indices) {
               if (events[i].location == str) {
                   newList_b.add(events[i])
               } else {
                   newList_n.add(events[i])
               }
           }

           Log.d("tagggg", ".....dd....." + newList + "...." + newList_b + "....." + newList_n)
   */

        val newList = arrayListOf<Event>()
        val newList_b = arrayListOf<Event>()
        val newList_n = arrayListOf<Event>()
        for (element in events) {
            if (!newList.contains(element)) {
                newList.add(element)
            }
        }
        for (i in events.indices) {
            if (events[i].location == str) {
                newList_b.add(events[i])
            } else {
                newList_n.add(events[i])
            }
        }

        for(i in events.indices){
            for(j in newList_b.indices) {
                if (events[i].startTS.equals(newList_b[j])){
                }
            }
        }
        Log.d("tgggggg","bbb......"+newList)
        Log.d("tgggggg","bbb......"+newList_b)
        Log.d("tgggggg","bbb......"+newList_n)
        DayEventsAdapter(activity as SimpleActivity, events, mHolder.day_events) {
            editEvent(it as Event)
        }.apply {
            mHolder.day_events.adapter = this
        }
    }

    private fun editEvent(event: Event) {
        Intent(context, AppointmentFormActivity::class.java).apply {
            putExtra(EVENT_ID, event.id)
            putExtra(EVENT_OCCURRENCE_TS, event.startTS)
            startActivity(this)
        }
    }
}
