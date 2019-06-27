package com.jeongwoochang.sleepapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jeongwoochang.sleepapp.API.APIClient
import com.jeongwoochang.sleepapp.API.APIInterface
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.adapter.viewholder.ShareTODOHolder
import com.jeongwoochang.sleepapp.util.data.Board
import com.jeongwoochang.sleepapp.util.data.BoardRes
import com.jeongwoochang.sleepapp.util.data.LoginRes
import com.jeongwoochang.sleepapp.util.data.UserRes
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_board_detail.*
import okhttp3.OkHttpClient
import org.joda.time.Interval
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BoardDetailActivity : AppCompatActivity() {

    private lateinit var data: Board
    private var position: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        data = (intent.getSerializableExtra("board") as Board)
        position = intent.getIntExtra("position", -1)
        val service = APIClient.getClient(this).create(APIInterface::class.java)
        service.boards.enqueue(object : Callback<BoardRes> {
            override fun onFailure(call: Call<BoardRes>, t: Throwable) {
                execption()
            }

            override fun onResponse(call: Call<BoardRes>, response: Response<BoardRes>) {
                data = response.body()!!.data[position]
            }

        })
        if (position == -1) {
            execption()
        }
        val pd = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setDetailsLabel("Loading...")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
        val picasso: Picasso
        val client = OkHttpClient()
        picasso = Picasso.Builder(this).downloader(OkHttp3Downloader(client)).build()
        service.getUser(data.id).enqueue(object : retrofit2.Callback<UserRes> {
            override fun onResponse(call: Call<UserRes>, response: Response<UserRes>) {
                val data = response.body()
                username.text = data!!.username
                Timber.d(data.photo)
                picasso.load("https://good-night-image-bucket.s3.ap-northeast-2.amazonaws.com/" + data.photo)
                    .into(profileImage, object :
                        com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            pd.dismiss()
                        }

                        override fun onError(e: Exception) {
                            pd.dismiss()
                            e.printStackTrace()
                        }
                    })
            }

            override fun onFailure(call: Call<UserRes>, t: Throwable) {
                pd.dismiss()
            }
        })
        Picasso.get()
            .load("https://good-night-board-image-bucket.s3.ap-northeast-2.amazonaws.com/" + data.photo)
            .into(profileImage, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Timber.d("Success")
                    Picasso.get()
                        .load("https://good-night-board-image-bucket.s3.ap-northeast-2.amazonaws.com/" + data.photo)
                        .into(boardImage, object : com.squareup.picasso.Callback {
                            override fun onSuccess() {
                                Timber.d("Success")
                                pd.dismiss()
                            }

                            override fun onError(e: Exception) {
                                e.printStackTrace()
                                pd.dismiss()
                                execption()
                            }
                        })
                }

                override fun onError(e: Exception) {
                    e.printStackTrace()
                }
            })
        titleTV.text = data.title
        username.text = data.id
        content.text = data.body_text
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            boardDate.text = getSummaryPeriod(sdf.parse(data.date))
        } catch (e: ParseException) {
            e.printStackTrace()
            boardDate.text = "날짜 형식 오류"
        }

        val likes = data.likes
        if (likes != null) {
            like.text = likes.size.toString()
        }
        val likeIsClicked = likes.contains(SharedPreferencesHelper(this).email)
        if (likeIsClicked) {
            unlikeBtn.visibility = View.VISIBLE
            likeBtn.visibility = View.GONE
        } else {
            unlikeBtn.visibility = View.GONE
            likeBtn.visibility = View.VISIBLE
        }
        likeArea.setOnClickListener {
            service.boards.enqueue(object : Callback<BoardRes> {
                override fun onResponse(call: Call<BoardRes>, response: Response<BoardRes>) {
                    if (response.body()!!.isStatus) {
                        if (response.body()!!.data[position].likes.contains(
                                SharedPreferencesHelper(this@BoardDetailActivity).email
                            )
                        ) { // 종아요 눌려 있음 => 해제
                            service.unclickLike(data.no.toString()).enqueue(object : Callback<LoginRes> {
                                override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                                    if (response.body()!!.isStatus) {
                                        Toast.makeText(this@BoardDetailActivity, "좋아요가 반영되었습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                        like.text = (Integer.valueOf(like.text.toString()) - 1).toString()
                                        unlikeBtn.visibility = View.GONE
                                        likeBtn.visibility = View.VISIBLE
                                    } else {
                                        Toast.makeText(this@BoardDetailActivity, "좋아요가 반영되지 않았습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginRes>, t: Throwable) {

                                }
                            })
                        } else { // 종아요 눌려 있지 않음 => 누름
                            service.clickLike(data.no.toString()).enqueue(object : retrofit2.Callback<LoginRes> {
                                override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                                    if (response.body()!!.isStatus) {
                                        Toast.makeText(this@BoardDetailActivity, "좋아요가 반영되었습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                        like.text =
                                            (Integer.valueOf(like.text.toString()) + 1).toString()
                                        unlikeBtn.visibility = View.VISIBLE
                                        likeBtn.visibility = View.GONE
                                    } else {
                                        Toast.makeText(this@BoardDetailActivity, "좋아요가 반영되지 않았습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginRes>, t: Throwable) {

                                }
                            })
                        }
                    }
                }

                override fun onFailure(call: Call<BoardRes>, t: Throwable) {

                }
            })
        }
    }

    private fun execption() {
        Toast.makeText(this, "게시글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getSummaryPeriod(date: Date): String {
        var resultPeriod = ""
        val interval = Interval(date.time, Date().time)
        val period = interval.toPeriod()
        if (period.years > 0) {
            resultPeriod = period.years.toString() + "년 전"
        } else if (period.months > 0) {
            resultPeriod = period.months.toString() + "개월 전"
        } else if (period.weeks > 0) {
            resultPeriod = period.weeks.toString() + "주 전"
        } else if (period.hours > 0) {
            resultPeriod = period.days.toString() + "일 전"
        } else if (period.weeks > 0) {
            resultPeriod = period.hours.toString() + "시간 전"
        } else if (period.minutes > 0) {
            resultPeriod = period.minutes.toString() + "분 전"
        } else if (period.seconds > 0) {
            resultPeriod = period.seconds.toString() + "초 전"
        }
        return resultPeriod
    }
}
