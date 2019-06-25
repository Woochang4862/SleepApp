package com.jeongwoochang.sleepapp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.jeongwoochang.sleepapp.API.APIClient
import com.jeongwoochang.sleepapp.API.APIInterface
import com.jeongwoochang.sleepapp.R
import com.jeongwoochang.sleepapp.activity.AddTODOActivity
import com.jeongwoochang.sleepapp.activity.ShareTODOActivity
import com.jeongwoochang.sleepapp.adapter.EditTODOListActivity
import com.jeongwoochang.sleepapp.adapter.TODOListAdapter
import com.jeongwoochang.sleepapp.util.data.UserRes
import com.jeongwoochang.sleepapp.util.helper.data.DBAdapter
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.android.synthetic.main.fragment_todo.view.*
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception


class TODOFragment : Fragment(), DatePickerListener {
    private lateinit var viewOfLayout: View
    private lateinit var todoListAdapter: TODOListAdapter
    private var dateSelected = DateTime()
    private lateinit var service: APIInterface
    private lateinit var pref: SharedPreferencesHelper

    companion object {

        fun newInstance(): TODOFragment {
            val args = Bundle()

            val fragment = TODOFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_todo, container, false)
        val cd = DateTime()
        service = APIClient.getClient(context).create(APIInterface::class.java)
        pref = SharedPreferencesHelper(context!!)
        service.getUser(pref.email).enqueue(object : Callback<UserRes> {
            override fun onFailure(call: Call<UserRes>, t: Throwable) {
            }

            override fun onResponse(call: Call<UserRes>, response: Response<UserRes>) {
                val data = response.body()
                viewOfLayout.userName.text = data!!.username
                val pd = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Loading...")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show()
                Picasso.get().load("https://good-night-image-bucket.s3.ap-northeast-2.amazonaws.com/" + data.photo)
                    .into(viewOfLayout.profileImg, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            pd.dismiss()
                        }

                        override fun onError(e: Exception?) {
                            Toast.makeText(context, "회원 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                            pd.dismiss()
                        }
                    })
            }
        })
        viewOfLayout.datePicker
            .setListener(this)
            .setDays(cd.dayOfMonth().maximumValue) //마지막 날짜
            .setOffset(cd.dayOfMonth - 1) //오늘 날짜
            .setDateSelectedColor(getColor(activity!!, R.color.todo_primary_color))
            .setDateSelectedTextColor(Color.WHITE)
            .setTodayButtonTextColor(getColor(activity!!, R.color.colorPrimary))
            .setTodayDateTextColor(getColor(activity!!, R.color.colorPrimary))
            .setTodayDateTextColor(Color.WHITE)
            .setTodayDateBackgroundColor(getColor(activity!!, R.color.todo_primary_dark_color))
            .setDayOfWeekTextColor(getColor(activity!!, R.color.main_textcolor))
            .setUnselectedDayTextColor(getColor(activity!!, R.color.white))
            .showTodayButton(true)
            .setMonthAndYearTextColor(getColor(activity!!, R.color.colorPrimary))
            .setTodayButtonTextColor(getColor(activity!!, R.color.main_textcolor))
            .init()
        viewOfLayout.datePicker.setDate(DateTime())
        viewOfLayout.datePicker.backgroundColor = getColor(activity!!, R.color.default_background_color)
        val dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(context)
        dbAdapter.getTODO(cd).size
        val text = SpannableString.valueOf(
            String.format(
                getString(R.string.number_of_todo_text),
                dbAdapter.getTODO(DateTime()).size
            )
        )
        text.setSpan(
            ForegroundColorSpan(getColor(activity!!, R.color.todo_primary_color)),
            text.indexOf("${dbAdapter.getTODO(cd).size}개의 과제"),
            text.indexOf("${dbAdapter.getTODO(cd).size}개의 과제") + 6,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        dbAdapter.close()
        viewOfLayout.numberOfTODO.text = text
        todoListAdapter = TODOListAdapter()
        viewOfLayout.todoList.adapter = todoListAdapter
        viewOfLayout.todoList.layoutManager = LinearLayoutManager(context)
        viewOfLayout.todoListArea.setOnClickListener {
            val intent = Intent(activity, EditTODOListActivity::class.java)
            intent.putExtra(DBAdapter.TODO_DATE, dateSelected)
            startActivity(intent)
        }
        viewOfLayout.addBtn.setOnClickListener {
            startActivity(Intent(activity, AddTODOActivity::class.java))
        }
        viewOfLayout.shareBtn.setOnClickListener {
            startActivity(Intent(activity, ShareTODOActivity::class.java))
        }
        return viewOfLayout
    }

    private fun exception() {
        activity?.finish()
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        this.dateSelected = dateSelected!!
        val dbAdapter = DBAdapter.getInstance()
        DBAdapter.connect(context)
        if (dbAdapter.getTODO(dateSelected).isEmpty())
            todoListArea.visibility = View.GONE
        else
            todoListArea.visibility = View.VISIBLE
        todoListAdapter.setItems(dbAdapter.getTODO(dateSelected))
        todoListAdapter.notifyDataSetChanged()
        Timber.d(dbAdapter.getTODO(dateSelected).toString())
        dbAdapter.close()
    }

    override fun onResume() {
        super.onResume()
        viewOfLayout.datePicker.setDate(DateTime())
    }

}
