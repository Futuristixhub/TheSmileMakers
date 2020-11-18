package com.smilemakers.utils

import android.content.Intent
import com.simplemobiletools.commons.dialogs.WritePermissionDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.OPEN_DOCUMENT_TREE
import com.simplemobiletools.commons.helpers.OPEN_DOCUMENT_TREE_OTG
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.commons.models.FileDirItem
import com.smilemakers.BuildConfig
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment.Event
import com.smilemakers.ui.dashBoard.appointmentFragment.calendar.SimpleActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.collections.ArrayList

fun SimpleActivity.shareEvents(ids: List<Long>) {
    ensureBackgroundThread {
        val file = getTempFile()
        if (file == null) {
            toast(R.string.unknown_error_occurred)
            return@ensureBackgroundThread
        }

        val events = eventsDB.getEventsWithIds(ids) as ArrayList<Event>
        IcsExporter().exportEvents(this, file, events, false) {
            if (it == IcsExporter.ExportResult.EXPORT_OK) {
                sharePathIntent(file.absolutePath, BuildConfig.APPLICATION_ID)
            }
        }
    }
}

fun SimpleActivity.getTempFile(): File? {
    val folder = File(cacheDir, "events")
    if (!folder.exists()) {
        if (!folder.mkdir()) {
            toast(R.string.unknown_error_occurred)
            return null
        }
    }

    return File(folder, "events.ics")
}

fun SimpleActivity.isShowingSAFDialog(path: String): Boolean {
    return if (isPathOnSD(path) && !isSDCardSetAsDefaultStorage() && (baseConfig.treeUri.isEmpty() || !hasProperStoredTreeUri(false))) {
        runOnUiThread {
            if (!isDestroyed && !isFinishing) {
                WritePermissionDialog(this, false) {
                    Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                        putExtra("android.content.extra.SHOW_ADVANCED", true)
                        if (resolveActivity(packageManager) == null) {
                            type = "*/*"
                        }

                        if (resolveActivity(packageManager) != null) {
                            checkedDocumentPath = path
                            startActivityForResult(this, OPEN_DOCUMENT_TREE)
                        } else {
                            toast(R.string.unknown_error_occurred)
                        }
                    }
                }
            }
        }
        true
    } else {
        false
    }
}

fun SimpleActivity.isShowingOTGDialog(path: String): Boolean {
    return if (isPathOnOTG(path) && (baseConfig.OTGTreeUri.isEmpty() || !hasProperStoredTreeUri(true))) {
        showOTGPermissionDialog(path)
        true
    } else {
        false
    }
}

fun SimpleActivity.showOTGPermissionDialog(path: String) {
    runOnUiThread {
        if (!isDestroyed && !isFinishing) {
            WritePermissionDialog(this, true) {
                Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                    if (resolveActivity(packageManager) == null) {
                        type = "*/*"
                    }

                    if (resolveActivity(packageManager) != null) {
                        checkedDocumentPath = path
                        startActivityForResult(this, OPEN_DOCUMENT_TREE_OTG)
                    } else {
                        toast(R.string.unknown_error_occurred)
                    }
                }
            }
        }
    }
}

fun SimpleActivity.showFileCreateError(path: String) {
    val error = String.format(getString(R.string.could_not_create_file), path)
    baseConfig.treeUri = ""
    showErrorToast(error)
}

fun SimpleActivity.getFileOutputStream(fileDirItem: FileDirItem, allowCreatingNewFile: Boolean = false, callback: (outputStream: OutputStream?) -> Unit) {
    if (needsStupidWritePermissions(fileDirItem.path)) {
        handleSAFDialog(fileDirItem.path) {
            if (!it) {
                return@handleSAFDialog
            }

            var document = getDocumentFile(fileDirItem.path)
            if (document == null && allowCreatingNewFile) {
                document = getDocumentFile(fileDirItem.getParentPath())
            }

            if (document == null) {
                showFileCreateError(fileDirItem.path)
                callback(null)
                return@handleSAFDialog
            }

            if (!getDoesFilePathExist(fileDirItem.path)) {
                document = document.createFile("", fileDirItem.name) ?: getDocumentFile(fileDirItem.path)
            }

            if (document?.exists() == true) {
                try {
                    callback(applicationContext.contentResolver.openOutputStream(document.uri))
                } catch (e: FileNotFoundException) {
                    showErrorToast(e)
                    callback(null)
                }
            } else {
                showFileCreateError(fileDirItem.path)
                callback(null)
            }
        }
    } else {
        val file = File(fileDirItem.path)
        if (file.parentFile?.exists() == false) {
            file.parentFile.mkdirs()
        }

        try {
            callback(FileOutputStream(file))
        } catch (e: Exception) {
            callback(null)
        }
    }
}
