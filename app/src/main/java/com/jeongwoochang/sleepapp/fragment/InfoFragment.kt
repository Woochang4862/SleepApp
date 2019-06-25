package com.jeongwoochang.sleepapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.jeongwoochang.sleepapp.adapter.InfoActivity
import com.jeongwoochang.sleepapp.adapter.MenuListAdapter
import com.jeongwoochang.sleepapp.util.data.Menu
import kotlinx.android.synthetic.main.fragment_info.*
import gun0912.tedbottompicker.GridSpacingItemDecoration
import android.util.DisplayMetrics
import com.jeongwoochang.sleepapp.R


class InfoFragment : Fragment() {

    companion object {
        fun newInstance(): InfoFragment {
            val args = Bundle()

            val fragment = InfoFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuList.layoutManager = GridLayoutManager(context, 2)
        val adapter = MenuListAdapter(
            arrayListOf(
                Menu("Small Single", R.drawable.ex_menu_image),
                Menu("Twin | Singles", R.drawable.illustration_twin_single),
                Menu("Full | Double", R.drawable.illustration_full_double),
                Menu("Queen", R.drawable.illustration_queen),
                Menu("King", R.drawable.illustration_king),
                Menu("California King", R.drawable.illustration_california_king),
                Menu("Toddlers Bed | Cribs", R.drawable.illustration_toddle_bed_cribs)
            )
        )
        adapter.setOnItemClickListener { v, position ->
            val i = Intent(activity, InfoActivity::class.java)
            i.putExtra("position", position)
            startActivity(i)
        }
        menuList.adapter = adapter
        val spanCount = 2
        val spacing = dpToPx(context!!, 50)
        val includeEdge = true
        menuList.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
    }

    fun dpToPx(context: Context, dp: Int): Int {

        val displayMetrics = context.resources.displayMetrics

        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))

    }

}