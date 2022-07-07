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

/**
 * description :数据库管理类,负责管理数据库的创建、升级工作
 */
class MySqliteOpenHelper(private val context: Context) : SQLiteOpenHelper(
    context, DB_NAME, null, DB_VERSION
) {
    /**
     * 在数据库首次创建的时候调用，创建表以及可以进行一些表数据的初始化
     *
     * @param db
     */
    override fun onCreate(db: SQLiteDatabase) {
        //创建表
        //_id为主键并且自增长一般命名为_id
        val newsSql =
            "create table news(id integer primary key autoincrement,typeId,title,img,content,issuer,date)"
        val userSql =
            "create table user(id integer primary key autoincrement,account, password,name,sex, phone,address,photo)"
        db.execSQL(newsSql)
        db.execSQL(userSql)
    }

    /**
     * 数据库升级的时候回调该方法，在数据库版本号DB_VERSION升级的时候才会调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        //给表添加一个字段
        //db.execSQL("alter table person add age integer");
    }

    /**
     * 数据库打开的时候回调该方法
     *
     * @param db
     */
    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
    }

    companion object {
        //数据库名字
        const val DB_NAME = "news.db"

        //数据库版本
        const val DB_VERSION = 1
    }
}