package com.smilemakers.dashBoard.appointmentFragment

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.smilemakers.utils.REGULAR_EVENT_TYPE_ID
import com.smilemakers.utils.REMINDER_NOTIFICATION
import com.smilemakers.utils.SOURCE_SIMPLE_CALENDAR

data class Events(var event:Event,
    var eventlst: ArrayList<Event>
)
