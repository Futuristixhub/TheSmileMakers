package com.smilemakers.ui.dashBoard.appointmentFragment.calendar

import org.joda.time.DateTime

interface NavigationListener {
    fun goLeft()

    fun goRight()

    fun goToDateTime(dateTime: DateTime)
}
