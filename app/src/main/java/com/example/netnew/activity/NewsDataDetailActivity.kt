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
import android.view.View

class NewsDataDetailActivity : AppCompatActivity() {
    private var myActivity: Activity? = null
    private var webView: WebView? = null
    private var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myActivity = this
        setContentView(R.layout.activity_news_data_detail)
        url = intent.getStringExtra("url")
        webView = findViewById(R.id.webView)
        initData()
    }

    private fun initData() {
        val webSettings = webView!!.settings
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.javaScriptEnabled = true
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式
        webSettings.domStorageEnabled = true
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(wv: WebView, url: String): Boolean {
                wv.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed()
            }
        }
        webView!!.loadUrl(url!!)
    }

    //返回
    fun back(view: View?) {
        finish()
    }
}