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
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 数据持久化工具类
 */
object SPUtils {
    const val IF_FIRST = "is_first" //是否第一次进来
    const val IS_ADMIN = "is_admin" //是否是管理员
    const val USER_ID = "USER_ID" //账号

    /**
     * 保存在手机里面的文件名
     */
    private const val FILE_NAME = "share_data"

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context 上下文
     * @param key     key值
     * @param object  value值
     */
    fun put(context: Context, key: String?, `object`: Any) {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        if (`object` is String) {
            editor.putString(key, `object`)
        } else if (`object` is Int) {
            editor.putInt(key, `object`)
        } else if (`object` is Boolean) {
            editor.putBoolean(key, `object`)
        } else if (`object` is Float) {
            editor.putFloat(key, `object`)
        } else if (`object` is Long) {
            editor.putLong(key, `object`)
        } else {
            editor.putString(key, `object`.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context       上下文
     * @param key           key值
     * @param defaultObject 默认value值
     * @return value值
     */
    operator fun get(context: Context, key: String?, defaultObject: Any?): Any? {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        if (defaultObject is String) {
            return sp.getString(key, defaultObject as String?)
        } else if (defaultObject is Int) {
            return sp.getInt(key, (defaultObject as Int?)!!)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, (defaultObject as Boolean?)!!)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, (defaultObject as Float?)!!)
        } else if (defaultObject is Long) {
            return sp.getLong(key, (defaultObject as Long?)!!)
        }
        return null
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context 上下文
     * @param key     key值
     */
    fun remove(context: Context, key: String?) {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据
     *
     * @param context 上下文
     */
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context 上下文
     * @param key     key值
     * @return 是否存在
     */
    fun contains(context: Context, key: String?): Boolean {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @param context 上下文
     * @return 所有的键值对
     */
    fun getAll(context: Context): Map<String, *> {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.all
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return Method
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor SharedPreferences.Edito
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            editor.commit()
        }
    }
}