package com.example.netnew.util

import okhttp3.logging.HttpLoggingInterceptor.setLevel
import okhttp3.HttpUrl.host
import okhttp3.OkHttpClient.Builder.connectTimeout
import okhttp3.OkHttpClient.Builder.writeTimeout
import okhttp3.OkHttpClient.Builder.readTimeout
import okhttp3.OkHttpClient.Builder.addInterceptor
import okhttp3.OkHttpClient.Builder.cookieJar
import okhttp3.OkHttpClient.Builder.build
import okhttp3.Request.Builder.url
import okhttp3.Request.Builder.build
import okhttp3.FormBody.Builder.add
import okhttp3.FormBody.Builder.build
import okhttp3.Request.Builder.post
import okhttp3.MultipartBody.Builder.setType
import okhttp3.MultipartBody.Builder.addFormDataPart
import okhttp3.MultipartBody.Builder.build
import okhttp3.OkHttpClient.newCall
import okhttp3.Call.enqueue
import okhttp3.Response.code
import okhttp3.Response.body
import okhttp3.Response.isSuccessful
import okhttp3.ResponseBody.string
import okhttp3.Request.Builder.addHeader
import android.content.SharedPreferences
import com.example.netnew.util.OkHttpTool.ResponseCallback
import com.example.netnew.util.OkHttpTool
import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.CookieJar
import okhttp3.Cookie
import android.graphics.Bitmap
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.BitmapCallback
import com.luck.picture.lib.engine.ImageEngine
import com.bumptech.glide.Glide
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.bumptech.glide.request.target.ImageViewTarget
import android.graphics.drawable.Drawable
import com.luck.picture.lib.tools.MediaUtils
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.ImageViewState
import android.graphics.PointF
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.netnew.util.GlideEngine
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import android.database.sqlite.SQLiteOpenHelper
import com.example.netnew.util.MySqliteOpenHelper
import android.database.sqlite.SQLiteDatabase

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            //说明当前有网络
            if (networkInfo != null && networkInfo.isAvailable) {
                val type = networkInfo.type
                when (type) {
                    ConnectivityManager.TYPE_MOBILE -> Toast.makeText(
                        context,
                        "当前移动网络正常",
                        Toast.LENGTH_SHORT
                    ).show()
                    ConnectivityManager.TYPE_WIFI -> Toast.makeText(
                        context,
                        "当前WIFI网络正常",
                        Toast.LENGTH_SHORT
                    ).show()
                    ConnectivityManager.TYPE_ETHERNET -> Toast.makeText(
                        context,
                        "当前以太网网络正常",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                //说明当前没有网络
                Toast.makeText(context, "当前网络异常", Toast.LENGTH_SHORT).show()
            }
        }
    }
}