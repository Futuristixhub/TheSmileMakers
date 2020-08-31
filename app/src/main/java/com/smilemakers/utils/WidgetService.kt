package com.smilemakers.utils

import android.content.Intent
import android.widget.RemoteViewsService
import com.simplemobiletools.calendar.pro.adapters.EventListWidgetAdapter

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapter(applicationContext)
}
