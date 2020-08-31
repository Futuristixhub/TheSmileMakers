package com.smilemakers.utils

import android.app.IntentService
import android.content.Intent

class SnoozeService : IntentService("Snooze") {

    override fun onHandleIntent(intent: Intent?) {
        val eventId = intent!!.getLongExtra(EVENT_ID, 0L)
        val event = eventsDB.getEventWithId(eventId)
        rescheduleReminder(event, config.snoozeTime)
    }
}
