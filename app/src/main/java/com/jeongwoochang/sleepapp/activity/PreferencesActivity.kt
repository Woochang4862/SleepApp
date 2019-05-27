package com.jeongwoochang.sleepapp.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jeongwoochang.sleepapp.fragment.PreferencesFragment

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, PreferencesFragment()).commit()
    }
}
