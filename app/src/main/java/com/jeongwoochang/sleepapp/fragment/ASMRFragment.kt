package com.jeongwoochang.sleepapp.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.adapter.ASMRFragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_asmr.*
import kotlinx.android.synthetic.main.fragment_asmr.view.*
import timber.log.Timber

class ASMRFragment : Fragment() {

    private lateinit var viewOfLayout: View

    companion object {
        fun newInstance(): ASMRFragment {
            val args = Bundle()

            val fragment = ASMRFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_asmr, container, false)

        //TabLayout Setting
        viewOfLayout.asmrTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabIconColor = ContextCompat.getColor(context!!, R.color.slate)
                tab?.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabIconColor = ContextCompat.getColor(context!!, R.color.tabLayoutIconSelected)
                tab?.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }
        })

        //ViewPager Setting
        viewOfLayout.asmrPager.adapter = ASMRFragmentPagerAdapter(childFragmentManager)

        viewOfLayout.asmrTabs.setupWithViewPager(viewOfLayout.asmrPager)
        viewOfLayout.asmrTabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_asmr_2)
        viewOfLayout.asmrTabs.getTabAt(0)?.icon?.setColorFilter(
            ContextCompat.getColor(
                context!!,
                R.color.tabLayoutIconSelected
            ), PorterDuff.Mode.SRC_IN
        )
        viewOfLayout.asmrTabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_white_noise)
        viewOfLayout.asmrTabs.getTabAt(1)?.icon?.setColorFilter(
            ContextCompat.getColor(context!!, R.color.slate),
            PorterDuff.Mode.SRC_IN
        )
        return viewOfLayout
    }

}