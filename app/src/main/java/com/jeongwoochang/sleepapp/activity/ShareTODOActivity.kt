package com.jeongwoochang.sleepapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeongwoochang.sleepapp.API.APIClient
import com.jeongwoochang.sleepapp.API.APIInterface
import com.jeongwoochang.sleepapp.util.data.BoardRes
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.adapter.ShareTODOListAdapter
import com.jeongwoochang.sleepapp.util.data.LoginRes
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_share_todo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShareTODOActivity : AppCompatActivity() {

    private lateinit var adapter: ShareTODOListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_todo)

        writeBoardBtn.setOnClickListener {
            startActivity(Intent(this, WriteTODOActivity::class.java))
        }
        val service = APIClient.getClient(this).create(APIInterface::class.java)
        adapter = ShareTODOListAdapter(this)
        service.boards.enqueue(object : Callback<BoardRes> {
            override fun onFailure(call: Call<BoardRes>, t: Throwable) {
                Toast.makeText(this@ShareTODOActivity, "게시판 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<BoardRes>, response: Response<BoardRes>) {
                val data = response.body() as BoardRes
                if (data.isStatus) {
                    adapter.setItemLimit(data.data.size)
                    adapter.items = data.data
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        })
        adapter.setOnItemClickListener { v, position ->
            val intent = Intent(this, BoardDetailActivity::class.java)
            intent.putExtra("board", adapter.items[position])
            intent.putExtra("position", position)
            startActivity(intent)
        }
        boardList.layoutManager = LinearLayoutManager(this)
        boardList.adapter = adapter
    }
}
