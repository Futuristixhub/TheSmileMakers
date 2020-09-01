package com.smilemakers.utils

import android.accounts.Account
import android.annotation.SuppressLint
import android.app.*
import android.appwidget.AppWidgetManager
import android.content.*
import android.media.AudioAttributes
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.util.Range
import android.view.Display
import android.widget.Toast
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import com.simplemobiletools.calendar.pro.helpers.MyWidgetListProvider
import com.simplemobiletools.calendar.pro.helpers.MyWidgetMonthlyProvider
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.*
import com.smilemakers.R
import com.smilemakers.dashBoard.appointmentFragment.*
import com.smilemakers.utils.Formatter.getDateFromCode
import com.smilemakers.utils.Formatter.getDateFromTS
import com.smilemakers.utils.Formatter.getDayCodeFromTS
import com.smilemakers.utils.Formatter.getLocalDateTimeFromCode
import com.smilemakers.utils.Formatter.getTimeFromTS
import kotlinx.android.synthetic.main.fragment_month.view.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.HashSet

val Context.config: Config get() = Config.newInstance(applicationContext)
val Context.eventsHelper: EventsHelper get() = EventsHelper(this)
val Context.eventsDB: EventsDao get() = EventsDatabase.getInstance(applicationContext).EventsDao()
val Context.eventTypesDB: EventTypesDao get() = EventsDatabase.getInstance(applicationContext).EventTypesDao()
val Context.calDAVHelper: CalDAVHelper get() = CalDAVHelper(this)


fun Context.updateWidgets() {
    val widgetIDs = AppWidgetManager.getInstance(applicationContext).getAppWidgetIds(ComponentName(applicationContext, MyWidgetMonthlyProvider::class.java))
    if (widgetIDs.isNotEmpty()) {
        Intent(applicationContext, MyWidgetMonthlyProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIDs)
            sendBroadcast(this)
        }
    }

    updateListWidget()
}

fun Context.recheckCalDAVCalendars(callback: () -> Unit) {
    if (config.caldavSync) {
        ensureBackgroundThread {
            calDAVHelper.refreshCalendars(false, callback)
            updateWidgets()
        }
    }
}

fun Context.updateListWidget() {
    val widgetIDs = AppWidgetManager.getInstance(applicationContext).getAppWidgetIds(ComponentName(applicationContext, MyWidgetListProvider::class.java))
    if (widgetIDs.isNotEmpty()) {
        Intent(applicationContext, MyWidgetListProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIDs)
            sendBroadcast(this)
        }
    }
}

fun Context.scheduleNextEventReminder(event: Event, showToasts: Boolean) {
    val validReminders = event.getReminders().filter { it.type == REMINDER_NOTIFICATION }
    if (validReminders.isEmpty()) {
        if (showToasts) {
            toast(R.string.saving)
        }
        return
    }

    val now = getNowSeconds()
    val reminderSeconds = validReminders.reversed().map { it.minutes * 60 }
    eventsHelper.getEvents(now, now + YEAR, event.id!!, false) {
        if (it.isNotEmpty()) {
            for (curEvent in it) {
                for (curReminder in reminderSeconds) {
                    if (curEvent.getEventStartTS() - curReminder > now) {
                        scheduleEventIn((curEvent.getEventStartTS() - curReminder) * 1000L, curEvent, showToasts)
                        return@getEvents
                    }
                }
            }
        }

        if (showToasts) {
            toast(R.string.saving)
        }
    }
}

fun Context.scheduleEventIn(notifTS: Long, event: Event, showToasts: Boolean) {
    if (notifTS < System.currentTimeMillis()) {
        if (showToasts) {
            toast(R.string.saving)
        }
        return
    }

    val newNotifTS = notifTS + 1000
    if (showToasts) {
        val secondsTillNotification = (newNotifTS - System.currentTimeMillis()) / 1000
        val msg = String.format(getString(R.string.reminder_triggers_in), formatSecondsToTimeString(secondsTillNotification.toInt()))
        toast(msg)
    }

    val pendingIntent = getNotificationIntent(applicationContext, event)
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    try {
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, newNotifTS, pendingIntent)
    } catch (e: Exception) {
        showErrorToast(e)
    }
}

fun Context.scheduleCalDAVSync(activate: Boolean) {
    val syncIntent = Intent(applicationContext, CalDAVSyncReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, syncIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (activate) {
        val syncCheckInterval = 2 * AlarmManager.INTERVAL_HOUR
        try {
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + syncCheckInterval, syncCheckInterval, pendingIntent)
        } catch (ignored: Exception) {
        }
    } else {
        alarm.cancel(pendingIntent)
    }
}

fun Context.refreshCalDAVCalendars(ids: String, showToasts: Boolean) {
    val uri = CalendarContract.Calendars.CONTENT_URI
    val accounts = HashSet<Account>()
    val calendars = calDAVHelper.getCalDAVCalendars(ids, showToasts)
    calendars.forEach {
        accounts.add(Account(it.accountName, it.accountType))
    }

    Bundle().apply {
        putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
        putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
        accounts.forEach {
            ContentResolver.requestSync(it, uri.authority, this)
        }
    }
}

fun Context.rescheduleReminder(event: Event?, minutes: Int) {
    if (event != null) {
        applicationContext.scheduleEventIn(System.currentTimeMillis() + minutes * 60000, event, false)
        cancelNotification(event.id!!)
    }
}

fun Context.launchNewEventIntent(dayCode: String = Formatter.getTodayCode()) {
    Intent(applicationContext, AppointmentFormActivity::class.java).apply {
        putExtra(NEW_EVENT_START_TS, getNewEventTimestampFromCode(dayCode))
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(this)
    }
}

fun Context.handleEventDeleting(eventIds: List<Long>, timestamps: List<Long>, action: Int) {
    when (action) {
        DELETE_SELECTED_OCCURRENCE -> {
            eventIds.forEachIndexed { index, value ->
                eventsHelper.addEventRepetitionException(value, timestamps[index], true)
            }
        }
        DELETE_FUTURE_OCCURRENCES -> {
            eventIds.forEachIndexed { index, value ->
                eventsHelper.addEventRepeatLimit(value, timestamps[index])
            }
        }
        DELETE_ALL_OCCURRENCES -> {
            eventsHelper.deleteEvents(eventIds.toMutableList(), true)
        }
    }
}

fun Context.getNewEventTimestampFromCode(dayCode: String): Long {
    val defaultStartTime = config.defaultStartTime
    val currHour = DateTime(System.currentTimeMillis(), DateTimeZone.getDefault()).hourOfDay
    var dateTime = Formatter.getLocalDateTimeFromCode(dayCode).withHourOfDay(currHour)
    var newDateTime = dateTime.plusHours(1).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)

    if (defaultStartTime != -1) {
        val hours = defaultStartTime / 60
        val minutes = defaultStartTime % 60
        dateTime =Formatter.getLocalDateTimeFromCode(dayCode).withHourOfDay(hours).withMinuteOfHour(minutes)
        newDateTime = dateTime
    }

    // make sure the date doesn't change
    return newDateTime.withDate(dateTime.year, dateTime.monthOfYear, dateTime.dayOfMonth).seconds()
}

fun Range<Int>.touch(other: Range<Int>) = (upper > other.lower && lower < other.upper) || (other.upper > lower && other.lower < upper)

fun Context.getRepetitionText(seconds: Int) = when (seconds) {
    0 -> getString(R.string.no_repetition)
    DAY -> getString(R.string.daily)
    WEEK -> getString(R.string.weekly)
    MONTH -> getString(R.string.monthly)
    YEAR -> getString(R.string.yearly)
    else -> {
        when {
            seconds % YEAR == 0 -> resources.getQuantityString(R.plurals.years, seconds / YEAR, seconds / YEAR)
            seconds % MONTH == 0 -> resources.getQuantityString(R.plurals.months, seconds / MONTH, seconds / MONTH)
            seconds % WEEK == 0 -> resources.getQuantityString(R.plurals.weeks, seconds / WEEK, seconds / WEEK)
            else -> resources.getQuantityString(R.plurals.days, seconds / DAY, seconds / DAY)
        }
    }
}

private fun getPendingIntent(context: Context, event: Event): PendingIntent {
    val intent = Intent(context, AppointmentFormActivity::class.java)
    intent.putExtra(EVENT_ID, event.id)
    intent.putExtra(EVENT_OCCURRENCE_TS, event.startTS)
    return PendingIntent.getActivity(context, event.id!!.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
}


fun Context.notifyEvent(originalEvent: Event) {
    var event = originalEvent.copy()
    val currentSeconds = getNowSeconds()

    var eventStartTS = if (event.getIsAllDay()) Formatter.getDayStartTS(Formatter.getDayCodeFromTS(event.startTS)) else event.startTS
    // make sure refer to the proper repeatable event instance with "Tomorrow", or the specific date
    if (event.repeatInterval != 0 && eventStartTS - event.reminder1Minutes * 60 < currentSeconds) {
        val events = eventsHelper.getRepeatableEventsFor(currentSeconds - WEEK_SECONDS, currentSeconds + YEAR_SECONDS, event.id!!)
        for (currEvent in events) {
            eventStartTS = if (currEvent.getIsAllDay()) Formatter.getDayStartTS(Formatter.getDayCodeFromTS(currEvent.startTS)) else currEvent.startTS
            if (eventStartTS - currEvent.reminder1Minutes * 60 > currentSeconds) {
                break
            }

            event = currEvent
        }
    }

    val pendingIntent = getPendingIntent(applicationContext, event)
    val startTime = getTimeFromTS(applicationContext, event.startTS)
    val endTime = getTimeFromTS(applicationContext, event.endTS)
    val startDate = getDateFromTS(event.startTS)

    val displayedStartDate = when (startDate) {
        LocalDate.now() -> ""
        LocalDate.now().plusDays(1) -> getString(R.string.tomorrow)
        else -> "${getDateFromCode(this, getDayCodeFromTS(event.startTS))},"
    }

    val timeRange = if (event.getIsAllDay()) getString(R.string.all_day) else getFormattedEventTime(startTime, endTime)
    val descriptionOrLocation =  event.location
    val content = "$displayedStartDate $timeRange $descriptionOrLocation".trim()
    ensureBackgroundThread {
        val notification = getNotification(pendingIntent, event, content)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            if (notification != null) {
                notificationManager.notify(event.id!!.toInt(), notification)
            }
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }
}

private fun getFormattedEventTime(startTime: String, endTime: String) = if (startTime == endTime) startTime else "$startTime \u2013 $endTime"

@SuppressLint("NewApi")
fun Context.getNotification(pendingIntent: PendingIntent, event: Event, content: String, publicVersion: Boolean = false): Notification? {
    var soundUri = config.reminderSoundUri
    if (soundUri == SILENT) {
        soundUri = ""
    } else {
        grantReadUriPermission(soundUri)
    }

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    // create a new channel for every new sound uri as the new Android Oreo notification system is fundamentally broken
    if (soundUri != config.lastSoundUri || config.lastVibrateOnReminder != config.vibrateOnReminder) {
        if (!publicVersion) {
            if (isOreoPlus()) {
                val oldChannelId = "simple_calendar_${config.lastReminderChannel}_${config.reminderAudioStream}_${event.eventType}"
                notificationManager.deleteNotificationChannel(oldChannelId)
            }
        }

        config.lastVibrateOnReminder = config.vibrateOnReminder
        config.lastReminderChannel = System.currentTimeMillis()
        config.lastSoundUri = soundUri
    }

    val channelId = "simple_calendar_${config.lastReminderChannel}_${config.reminderAudioStream}_${event.eventType}"
    if (isOreoPlus()) {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setLegacyStreamType(config.reminderAudioStream)
            .build()

        val name = eventTypesDB.getEventTypeWithId(event.eventType)?.getDisplayTitle()
        val importance = NotificationManager.IMPORTANCE_HIGH
        NotificationChannel(channelId, name, importance).apply {
            setBypassDnd(true)
            enableLights(true)
            lightColor = event.color
            enableVibration(config.vibrateOnReminder)
            setSound(Uri.parse(soundUri), audioAttributes)
            try {
                notificationManager.createNotificationChannel(this)
            } catch (e: Exception) {
                showErrorToast(e)
                return null
            }
        }
    }

    val contentTitle = if (publicVersion) resources.getString(R.string.app_name) else event.title
    val contentText = if (publicVersion) resources.getString(R.string.public_event_notification_text) else content

    val builder = NotificationCompat.Builder(this, channelId)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setSmallIcon(R.drawable.ic_calendar)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setDefaults(Notification.DEFAULT_LIGHTS)
        .setCategory(Notification.CATEGORY_EVENT)
        .setAutoCancel(true)
        .setSound(Uri.parse(soundUri), config.reminderAudioStream)
        .setChannelId(channelId)
        .addAction(R.drawable.ic_snooze_vector, getString(R.string.snooze), getSnoozePendingIntent(this, event))

    if (config.vibrateOnReminder) {
        val vibrateArray = LongArray(2) { 500 }
        builder.setVibrate(vibrateArray)
    }

    if (!publicVersion) {
        val notification = getNotification(pendingIntent, event, content, true)
        if (notification != null) {
            builder.setPublicVersion(notification)
        }
    }

    val notification = builder.build()
    if (config.loopReminders) {
        notification.flags = notification.flags or Notification.FLAG_INSISTENT
    }
    return notification
}

private fun getSnoozePendingIntent(context: Context, event: Event): PendingIntent {
    val snoozeClass =
        if (context.config.useSameSnooze) SnoozeService::class.java else SnoozeReminderActivity::class.java
    val intent = Intent(context, snoozeClass).setAction("Snooze")
    intent.putExtra(EVENT_ID, event.id)
    return if (context.config.useSameSnooze) {
        PendingIntent.getService(
            context,
            event.id!!.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    } else {
        PendingIntent.getActivity(
            context,
            event.id!!.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}

private fun getNotificationIntent(context: Context, event: Event): PendingIntent {
    val intent = Intent(context, NotificationReceiver::class.java)
    intent.putExtra(EVENT_ID, event.id)
    intent.putExtra(EVENT_OCCURRENCE_TS, event.startTS)
    return PendingIntent.getBroadcast(context, event.id!!.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
}


fun Context.cancelNotification(id: Long) {
    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(id.toInt())
}

