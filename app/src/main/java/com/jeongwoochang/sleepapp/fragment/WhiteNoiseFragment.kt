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
import kotlinx.android.synthetic.main.fragment_white_noise.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WhiteNoiseFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var service: APIInterface
    private var nextToken: String = ""
    private var prevToken: String = ""

    companion object {
        fun newInstance(): WhiteNoiseFragment {
            val args = Bundle()

            val fragment = WhiteNoiseFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_white_noise, container, false)
        service = APIClient.getClient(context).create(APIInterface::class.java)
        viewOfLayout.whiteNoiseList.layoutManager = LinearLayoutManager(context)
        val ytasmrListAdapter = YTASMRListAdapter(viewOfLayout.whiteNoiseList)
        ytasmrListAdapter.setOnLoadMoreListener {
            service.getWhiteNoise(nextToken).enqueue(object : Callback<YTASMRRes> {
                override fun onFailure(call: Call<YTASMRRes>, t: Throwable) {

                }

                override fun onResponse(call: Call<YTASMRRes>, response: Response<YTASMRRes>) {
                    val data = response.body()
                    if (data!!.status) {
                        ytasmrListAdapter.addItems(ArrayList(data.data.items))
                        ytasmrListAdapter.notifyDataSetChanged()
                        ytasmrListAdapter.setLoaded()
                        nextToken = data.data.nextPageToken
                    } else {

                    }
                }
            })
        }
        service.getWhiteNoise(1).enqueue(object : Callback<YTASMRRes> {
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
        viewOfLayout.whiteNoiseList.adapter = ytasmrListAdapter
        viewOfLayout.swipeView.setOnRefreshListener {
            ytasmrListAdapter.notifyDataSetChanged()
            viewOfLayout.swipeView.isRefreshing = false
        }
        viewOfLayout.swipeView.setColorSchemeColors(Color.GRAY)
        viewOfLayout.swipeView.setDistanceToTriggerSync(20)
        viewOfLayout.swipeView.setSize(SwipeRefreshLayout.DEFAULT)
        return viewOfLayout
    }

}