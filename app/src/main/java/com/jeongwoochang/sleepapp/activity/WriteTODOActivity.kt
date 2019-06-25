package com.jeongwoochang.sleepapp.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.jeongwoochang.sleepapp.API.APIClient
import com.jeongwoochang.sleepapp.API.APIInterface
import com.jeongwoochang.sleepapp.util.data.LoginRes
import com.jeongwoochang.sleepapp.R
import com.kaopiz.kprogresshud.KProgressHUD
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_write_todo.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class WriteTODOActivity : AppCompatActivity() {

    private var currentProfileImgFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_todo)

        boardImage.setOnClickListener {
            TedPermission.with(this)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        TedBottomPicker.with(this@WriteTODOActivity)
                            .show {
                                boardImage.setImageURI(it)
                                currentProfileImgFile = File(it.path)
                            }
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        Toast.makeText(applicationContext, "권한을 주세요", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
        }

        writeBoardBtn.setOnClickListener {
            if (content.text.isNotEmpty()) {
                if (currentProfileImgFile != null) {
                    val pd = KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Loading...")
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show()
                    val service = APIClient.getClient(this).create(APIInterface::class.java)
                    val map = HashMap<String, RequestBody>()
                    map.put("title", RequestBody.create(MediaType.parse("text/plain"), titleTV.text.toString()))
                    map.put("bodyText", RequestBody.create(MediaType.parse("text/plain"), content.text.toString()))
                    map.put(
                        "photo\"; filename=\"content_img.png\"",
                        RequestBody.create(MediaType.parse("image/png"), currentProfileImgFile)
                    )
                    service.writeBoard(
                        map
                    ).enqueue(object : Callback<LoginRes> {
                        override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                            Toast.makeText(applicationContext, "글쓰기 실패", Toast.LENGTH_SHORT).show()
                            pd.dismiss()
                            finish()
                        }

                        override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                            if (response.body()!!.isStatus) {
                                Toast.makeText(applicationContext, "글쓰기 성공", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                                pd.dismiss()
                                finish()
                            }
                        }

                    })
                } else {
                    Toast.makeText(this, "사진을 선택해 주세요", Toast.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), "1자이상 작성해 주세요", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
