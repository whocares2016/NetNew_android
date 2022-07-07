package com.example.netnew.activity

import android.app.Activity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.RadioButton
import com.example.netnew.util.NetworkReceiver
import android.os.Bundle
import com.example.netnew.R
import com.example.netnew.MyApplication
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.graphics.drawable.Drawable
import com.example.netnew.fragment.NewsFragment
import com.example.netnew.fragment.UserFragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.netnew.util.MySqliteOpenHelper
import android.content.Intent
import com.example.netnew.activity.LoginActivity
import android.widget.EditText
import com.example.netnew.activity.RegisterActivity
import android.database.sqlite.SQLiteDatabase
import android.widget.VideoView
import com.example.netnew.activity.VideoActivity
import android.widget.RadioGroup
import com.bumptech.glide.request.RequestOptions
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.example.netnew.util.GlideEngine
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.entity.LocalMedia
import android.text.TextUtils
import android.os.Build
import com.luck.picture.lib.tools.PictureFileUtils
import com.bumptech.glide.Glide
import android.webkit.WebView
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.webkit.SslErrorHandler
import android.net.http.SslError
import android.os.Handler
import com.example.netnew.util.SPUtils
import java.text.SimpleDateFormat

class OpenActivity : AppCompatActivity() {
    var helper: MySqliteOpenHelper? = null
    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open)
        helper = MySqliteOpenHelper(this)
        Handler().postDelayed({
            val userId = SPUtils.get(this@OpenActivity, SPUtils.USER_ID, 0) as Int
            //两秒后跳转到主页面
            val intent2 = Intent()
            if (userId > 0) {
                intent2.setClass(this@OpenActivity, MainActivity::class.java)
            } else {
                intent2.setClass(this@OpenActivity, LoginActivity::class.java)
            }
            startActivity(intent2)
            finish()
        }, 2000)
    }
}