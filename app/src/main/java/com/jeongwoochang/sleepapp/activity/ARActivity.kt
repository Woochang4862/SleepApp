package com.jeongwoochang.sleepapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jeongwoochang.sleepapp.R
import com.losers.argraphlibrary.SupportingClasses.Utils
import kotlinx.android.synthetic.main.activity_ar.*
import com.losers.argraphlibrary.Modal.PlotGraph
import com.losers.argraphlibrary.SupportingClasses.VideoRecorder.VIDEO_QUALITY
import com.losers.argraphlibrary.Modal.GraphConfig

class ARActivity : AppCompatActivity() {

    private lateinit var data:ArrayList<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        data = intent.getSerializableExtra("data") as ArrayList<Double>
        if (!Utils.checkIsDeviceSuppotARcore(this)) {
            add_a_graph.visibility = View.GONE
        }
        add_a_graph.setOnClickListener { PlotGraph.get(applicationContext).loadGraph(getGraphConfig()) }
    }

    //configure the graph
    private fun getGraphConfig(): GraphConfig {
        return GraphConfig.newBuilder()
            .setGraphList(data) // list you want to add in real world
            .setEnableClassicPlatform(true) // if you want to add platform or not
            .setEnableLogging(true) // enable logging
            .setEnableVideo(true) //enable video recording of ar graph
            .setVideoQuality(VIDEO_QUALITY.QUALITY_720P) // video quality
            .build()
    }
}


