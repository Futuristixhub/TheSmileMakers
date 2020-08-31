package com.smilemakers.dashBoard.appointmentFragment

interface WeekFragmentListener {
    fun scrollTo(y: Int)

    fun updateHoursTopMargin(margin: Int)

    fun getCurrScrollY(): Int
}
