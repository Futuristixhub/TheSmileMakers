package com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment

data class ListEvent(var id: Long, var startTS: Long, var endTS: Long, var title: String, var description: String, var isAllDay: Boolean, var color: Int,
                     var location: String, var isPastEvent: Boolean, var isRepeatable: Boolean) : ListItem()
