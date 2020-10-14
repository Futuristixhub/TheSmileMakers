package com.smilemakers.dashBoard.appointmentFragment.calendar

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.provider.CalendarContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.extensions.baseConfig
import com.simplemobiletools.commons.extensions.getPermissionString
import com.simplemobiletools.commons.extensions.hasPermission
import com.simplemobiletools.commons.extensions.showErrorToast
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.smilemakers.R
import com.smilemakers.utils.config
import com.smilemakers.utils.isShowingOTGDialog
import com.smilemakers.utils.isShowingSAFDialog
import com.smilemakers.utils.refreshCalDAVCalendars

open class SimpleActivity : AppCompatActivity() {
    val CALDAV_REFRESH_DELAY = 3000L
    val calDAVRefreshHandler = Handler()
    var calDAVRefreshCallback: (() -> Unit)? = null
    var checkedDocumentPath = ""

    private val GENERIC_PERM_HANDLER = 100
    var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    var isAskingPermissions = false

    fun Context.syncCalDAVCalendars(callback: () -> Unit) {
        calDAVRefreshCallback = callback
        ensureBackgroundThread {
            val uri = CalendarContract.Calendars.CONTENT_URI
            contentResolver.unregisterContentObserver(calDAVSyncObserver)
            contentResolver.registerContentObserver(uri, false, calDAVSyncObserver)
            refreshCalDAVCalendars(config.caldavSyncedCalendarIds, true)
        }
    }

    fun BaseSimpleActivity.showFileCreateError(path: String) {
        val error = String.format(getString(R.string.could_not_create_file), path)
        baseConfig.treeUri = ""
        showErrorToast(error)
    }

    fun handleSAFDialog(path: String, callback: (success: Boolean) -> Unit): Boolean {
        return if (!packageName.startsWith("com.simplemobiletools")) {
            callback(true)
            false
        } else if (isShowingSAFDialog(path) || isShowingOTGDialog(path)) {
            BaseSimpleActivity.funAfterSAFPermission = callback
            true
        } else {
            callback(true)
            false
        }
    }

    fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit) {
        actionOnPermission = null
        if (hasPermission(permissionId)) {
            callback(true)
        } else {
            isAskingPermissions = true
            actionOnPermission = callback
            ActivityCompat.requestPermissions(this, arrayOf(getPermissionString(permissionId)), GENERIC_PERM_HANDLER)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        isAskingPermissions = false
        if (requestCode == GENERIC_PERM_HANDLER && grantResults.isNotEmpty()) {
            actionOnPermission?.invoke(grantResults[0] == 0)
        }
    }

    override fun onStop() {
        super.onStop()
        actionOnPermission = null
    }

    // caldav refresh content observer triggers multiple times in a row at updating, so call the callback only a few seconds after the (hopefully) last one
    private val calDAVSyncObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            if (!selfChange) {
                calDAVRefreshHandler.removeCallbacksAndMessages(null)
                calDAVRefreshHandler.postDelayed({
                    ensureBackgroundThread {
                        unregisterObserver()
                        calDAVRefreshCallback?.invoke()
                        calDAVRefreshCallback = null
                    }
                }, CALDAV_REFRESH_DELAY)
            }
        }
    }

    private fun unregisterObserver() {
        contentResolver.unregisterContentObserver(calDAVSyncObserver)
    }
}
