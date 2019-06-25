package com.jeongwoochang.sleepapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeongwoochang.sleepapp.fragment.InfoFragment
import com.jeongwoochang.sleepapp.fragment.WhiteNoiseFragment
import com.jeongwoochang.sleepapp.fragment.YTASMRFragment

class ASMRFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> YTASMRFragment.newInstance()
            1 -> WhiteNoiseFragment.newInstance()
            else -> InfoFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}
