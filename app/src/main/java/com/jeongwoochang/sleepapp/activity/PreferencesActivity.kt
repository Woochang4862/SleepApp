package com.jeongwoochang.sleepapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeongwoochang.sleepapp.fragment.PreferencesFragment

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, PreferencesFragment()).commit()
    }
}
