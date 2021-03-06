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
 * ????????????????????????
 */
object SPUtils {
    const val IF_FIRST = "is_first" //?????????????????????
    const val IS_ADMIN = "is_admin" //??????????????????
    const val USER_ID = "USER_ID" //??????

    /**
     * ?????????????????????????????????
     */
    private const val FILE_NAME = "share_data"

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param context ?????????
     * @param key     key???
     * @param object  value???
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
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param context       ?????????
     * @param key           key???
     * @param defaultObject ??????value???
     * @return value???
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
     * ????????????key?????????????????????
     *
     * @param context ?????????
     * @param key     key???
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
     * ??????????????????
     *
     * @param context ?????????
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
     * ????????????key??????????????????
     *
     * @param context ?????????
     * @param key     key???
     * @return ????????????
     */
    fun contains(context: Context, key: String?): Boolean {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.contains(key)
    }

    /**
     * ????????????????????????
     *
     * @param context ?????????
     * @return ??????????????????
     */
    fun getAll(context: Context): Map<String, *> {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.all
    }

    /**
     * ??????????????????SharedPreferencesCompat.apply????????????????????????
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * ????????????apply?????????
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
         * ?????????????????????apply?????????????????????commit
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