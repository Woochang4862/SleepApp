package com.jeongwoochang.sleepapp.fragment

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper
import java.util.prefs.Preferences

class PreferencesFragment : PreferenceFragment() {

    private val LOG_TAG = PreferencesFragment::class.java!!.simpleName
    private var sharPrefHelper: SharedPreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        sharPrefHelper = SharedPreferencesHelper(activity)
        findPreference(getString(R.string.key_logout)).onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                val pref = SharedPreferencesHelper(context)
                pref.email = ""
                pref.autoLogin = false
                activity.finishAffinity()
                true
            }
    }


}
