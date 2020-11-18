package com.smilemakers.ui.dashBoard.appointmentFragment.calendar

import android.content.Context
import org.joda.time.DateTime

interface MonthlyCalendar {
    fun updateMonthlyCalendar(context: Context, month: String, days: ArrayList<DayMonthly>, checkedEvents: Boolean,
                              currTargetDate: DateTime)
}
