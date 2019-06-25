package com.jeongwoochang.sleepapp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jeongwoochang.sleepapp.API.APIClient
import com.jeongwoochang.sleepapp.API.APIInterface
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.activity.YoutubePlayerActivity
import com.jeongwoochang.sleepapp.adapter.YTASMRListAdapter
import com.jeongwoochang.sleepapp.util.data.YTASMRRes
import kotlinx.android.synthetic.main.fragment_yt_asmr.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YTASMRFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var service: APIInterface
    private var nextToken: String = ""
    private var prevToken: String = ""

    companion object {
        fun newInstance(): YTASMRFragment {
            val args = Bundle()

            val fragment = YTASMRFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_yt_asmr, container, false)
        viewOfLayout.YTASMRList.layoutManager = LinearLayoutManager(context)
        service = APIClient.getClient(context).create(APIInterface::class.java)

        val ytasmrListAdapter = YTASMRListAdapter(viewOfLayout.YTASMRList)
        ytasmrListAdapter.setOnLoadMoreListener {
            service.getASMR(nextToken).enqueue(object : Callback<YTASMRRes> {
                override fun onFailure(call: Call<YTASMRRes>, t: Throwable) {

                }

                override fun onResponse(call: Call<YTASMRRes>, response: Response<YTASMRRes>) {
                    val data = response.body()
                    if (data!!.status) {
                        ytasmrListAdapter.addItems(ArrayList(data.data.items))
                        ytasmrListAdapter.notifyDataSetChanged()
                        nextToken = data.data.nextPageToken
                        ytasmrListAdapter.setLoaded()
                    } else {

                    }
                }
            })
        }
        service.getASMR(1).enqueue(object : Callback<YTASMRRes> {
            override fun onFailure(call: Call<YTASMRRes>, t: Throwable) {

            }

            override fun onResponse(call: Call<YTASMRRes>, response: Response<YTASMRRes>) {
                val data = response.body()
                if (data!!.status) {
                    ytasmrListAdapter.setItems(ArrayList(data.data.items))
                    ytasmrListAdapter.notifyDataSetChanged()
                    nextToken = data.data.nextPageToken
                    ytasmrListAdapter.setItemLimit(data.data.pageInfo.totalResults)
                } else {

                }
            }
        })
        ytasmrListAdapter.setOnItemClickListener { v, position ->
            val i = Intent(activity, YoutubePlayerActivity::class.java)
            i.putExtra("item", ytasmrListAdapter.getItem(position))
            startActivity(i)
        }
        viewOfLayout.YTASMRList.adapter = ytasmrListAdapter
        viewOfLayout.swipeView.setOnRefreshListener {
            ytasmrListAdapter.notifyDataSetChanged()
            viewOfLayout.swipeView.isRefreshing = false
        }
        viewOfLayout.swipeView.setColorSchemeColors(Color.GRAY)
        viewOfLayout.swipeView.setDistanceToTriggerSync(20)
        viewOfLayout.swipeView.setSize(SwipeRefreshLayout.DEFAULT)
        viewOfLayout.YTASMRList.requestLayout()
        return viewOfLayout
    }

}