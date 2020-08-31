package com.smilemakers.utils

import android.content.Intent
import android.widget.RemoteViewsService
import com.simplemobiletools.calendar.pro.adapters.EventListWidgetAdapterEmpty

class WidgetServiceEmpty : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapterEmpty(applicationContext)
}
