package com.smilemakers.dashBoard.appointmentFragment.calendar

import android.content.Context
import com.smilemakers.dashBoard.appointmentFragment.calendar.DayMonthly
import org.joda.time.DateTime

interface MonthlyCalendar {
    fun updateMonthlyCalendar(context: Context, month: String, days: ArrayList<DayMonthly>, checkedEvents: Boolean,
                              currTargetDate: DateTime)
}
