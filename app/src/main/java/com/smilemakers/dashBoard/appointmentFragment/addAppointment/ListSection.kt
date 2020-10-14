package com.smilemakers.dashBoard.appointmentFragment.addAppointment

import com.smilemakers.dashBoard.appointmentFragment.addAppointment.ListItem

data class ListSection(val title: String, val code: String, val isToday: Boolean, val isPastSection: Boolean) : ListItem()
