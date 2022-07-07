package com.example.netnew.activity

import android.app.Activity
import com.example.netnew.util.NetworkReceiver
import android.os.Bundle
import com.example.netnew.R
import com.example.netnew.MyApplication
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.graphics.drawable.Drawable
import com.example.netnew.fragment.NewsFragment
import com.example.netnew.fragment.UserFragment
import androidx.appcompat.app.AppCompatActivity
import com.example.netnew.util.MySqliteOpenHelper
import android.content.Intent
import com.example.netnew.activity.LoginActivity
import com.example.netnew.activity.RegisterActivity
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.example.netnew.activity.VideoActivity
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
import android.view.View
import android.widget.*
import java.util.*

class VideoActivity : AppCompatActivity() {
    private var mVideo: VideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        mVideo = findViewById<View>(R.id.video) as VideoView
        mVideo!!.setVideoURI(
            Uri.parse(
                "android.resource://" + packageName + "/" + lj[Random().nextInt(
                    lj.size
                )]
            )
        )
        val controller = MediaController(this)
        mVideo!!.setMediaController(controller)
        mVideo!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun back(view: View?) {
        finish()
    }

    companion object {
        var lj = intArrayOf(R.raw.yue)
    }
}