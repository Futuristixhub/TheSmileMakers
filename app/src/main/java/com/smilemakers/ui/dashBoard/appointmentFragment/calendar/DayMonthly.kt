package com.smilemakers.ui.dashBoard.appointmentFragment.calendar

import com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment.Event

data class DayMonthly(val value: Int, val isThisMonth: Boolean, val isToday: Boolean, val code: String, val weekOfYear: Int,
                      var dayEvents: ArrayList<Event>,
                      var indexOnMonthView: Int)
