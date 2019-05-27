package com.jeongwoochang.sleepapp.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.activity.SleepTimePreferenceActivity
import com.jeongwoochang.sleepapp.util.helper.data.FileManager
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper

class PreferencesFragment : PreferenceFragment() {

    private val LOG_TAG = PreferencesFragment::class.java!!.getSimpleName()
    private var sharPrefHelper: SharedPreferencesHelper? = null
    private var fileManager: FileManager? = null
    private var RINGTONE_FILENAME_KEY: String? = null
    private var SLEEP_TIME_KEY: String? = null
    private val PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 23
    private val SLEEP_PREF_REQUEST_CODE = 201
    private val FILE_CHOOSER_REQUEST_CODE = 202

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        sharPrefHelper = SharedPreferencesHelper(activity)
        RINGTONE_FILENAME_KEY = getString(R.string.key_ringtone_filename)
        SLEEP_TIME_KEY = resources.getString(R.string.key_sleep_time)
        showRingtoneName(sharPrefHelper!!.ringtoneFileName)

        val ringtonePreference = findPreference(RINGTONE_FILENAME_KEY)
        val sleepTimePreference = findPreference(SLEEP_TIME_KEY)
        ringtonePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            onRingtonePreferenceClicked()
            true
        }
        sleepTimePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            onSleepTimePreferenceClicked()
            true
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                // If request is cancelled, the result array is empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Permission granted")
                    openFileChooser()
                } else {
                    Log.d(LOG_TAG, "Permission denied")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
                val selectedFileUri = data!!.data
                Log.d(LOG_TAG, "File found: uri: " + selectedFileUri!!)
                fileManager = FileManager(activity)
                val chosenFileName = fileManager!!.getFileName(selectedFileUri)

                sharPrefHelper = SharedPreferencesHelper(activity)
                sharPrefHelper!!.ringtoneFileName = chosenFileName

                Log.d(LOG_TAG, "File found: displayName: " + fileManager!!.getFileName(selectedFileUri))

                // Android 7 gives only temporary access to files from another apps, so I have do copy them to my app dir
                fileManager!!.copyToAppDir(selectedFileUri, chosenFileName)

                showRingtoneName(chosenFileName)
            } else if (requestCode == SLEEP_PREF_REQUEST_CODE) {
                Log.d(
                    LOG_TAG,
                    "Sleep Time : [" + data!!.getStringExtra(resources.getString(R.string.key_sleep_start_time)) + "], [" + data!!.getStringExtra(
                        resources.getString(R.string.key_sleep_end_time)
                    ) + "]"
                )
                sharPrefHelper = SharedPreferencesHelper(activity)
            }
        }
    }

    private fun onRingtonePreferenceClicked() {
        // request permissions if user didn't grant them before
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
            Log.d(LOG_TAG, "Permission already granted")
            openFileChooser()
        }
    }

    private fun onSleepTimePreferenceClicked() {
        startActivityForResult(Intent(activity, SleepTimePreferenceActivity::class.java), SLEEP_PREF_REQUEST_CODE)
    }

    private fun showRingtoneName(ringtoneName: String) {
        var ringtoneName = ringtoneName
        val pref = findPreference(RINGTONE_FILENAME_KEY)
        if (ringtoneName == "") {
            ringtoneName = getString(R.string.preferences_default_ringtone_name)
        }
        pref.summary = ringtoneName
    }

    fun openFileChooser() {
        val intent = Intent()
            .setType("audio/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        val chooserTitle = getString(R.string.chooseringtone_chooser_title)
        startActivityForResult(Intent.createChooser(intent, chooserTitle), FILE_CHOOSER_REQUEST_CODE)
    }


}
