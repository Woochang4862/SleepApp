package com.jeongwoochang.sleepapp.util.helper.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel

/**
 * Created by Ilya Anshmidt on 11.11.2017.
 */

class FileManager(private val context: Context) {

    private val LOG_TAG = FileManager::class.java.simpleName

    fun copyToAppDir(uri: Uri, destinationFileName: String) {
        val runnable = Runnable {
            try {
                clearAppDir()

                val fileInputStream = context.contentResolver.openInputStream(uri) as FileInputStream
                val source = fileInputStream.channel

                val destinationFile = File(context.filesDir, destinationFileName)
                Log.d(LOG_TAG, "destinationFile: $destinationFile")
                val destination = FileOutputStream(destinationFile).channel

                destination.transferFrom(source, 0, source.size())

                Log.d(LOG_TAG, "destinationFile length: " + destinationFile.length())

            } catch (e: IOException) {
                Log.e(LOG_TAG, "Copying a file failed: $e")
            }
        }

        val t = Thread(runnable)
        t.start()
    }

    fun getFileName(uri: Uri): String {
        val uriString = uri.toString()
        var name = ""

        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } finally {
                cursor!!.close()
            }
        } else if (uriString.startsWith("file://")) {
            val ringtoneFile = File(uriString)
            name = ringtoneFile.name
        }
        return name
    }

    private fun clearAppDir() {
        val appDir = context.filesDir
        for (file in appDir.listFiles()) {
            if (!file.isDirectory) {
                file.delete()
            }
        }
    }


}
