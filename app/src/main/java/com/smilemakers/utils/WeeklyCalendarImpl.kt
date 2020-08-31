package com.smilemakers.utils

import android.content.Context
import com.simplemobiletools.commons.helpers.WEEK_SECONDS
import com.smilemakers.dashBoard.appointmentFragment.Event
import com.smilemakers.dashBoard.appointmentFragment.WeeklyCalendar
import java.util.*

class WeeklyCalendarImpl(val callback: WeeklyCalendar, val context: Context) {
    var mEvents = ArrayList<Event>()

    fun updateWeeklyCalendar(weekStartTS: Long) {
        val endTS = weekStartTS + WEEK_SECONDS
        context.eventsHelper.getEvents(weekStartTS, endTS) {
            mEvents = it
            callback.updateWeeklyCalendar(it)
        }
    }
}
