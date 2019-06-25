package com.jeongwoochang.sleepapp.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.jeongwoochang.sleepapp.R
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layouts = arrayListOf(
            R.layout.fragment_small_single,
            R.layout.fragment_twin_single,
            R.layout.fragment_full_single,
            R.layout.fragment_queen,
            R.layout.fragment_king,
            R.layout.fragment_california_king,
            R.layout.fragment_toddler_beds_cribs
        )
        content.addView(inflater.inflate(layouts[intent.getIntExtra("position", 0)], null))
    }
}
