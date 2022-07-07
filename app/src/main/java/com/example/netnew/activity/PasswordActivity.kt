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
import com.example.netnew.util.SPUtils

/**
 * 重置密码
 */
class PasswordActivity : AppCompatActivity() {
    var helper: MySqliteOpenHelper? = null
    private var activity: Activity? = null
    private var etNewPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_password)
        //val temp = activity
        helper = MySqliteOpenHelper(activity!!)
        etNewPassword = findViewById(R.id.et_new_password)
    }

    //保存信息
    fun save(v: View?) {
        val db = helper!!.writableDatabase
        val newPassword = etNewPassword!!.text.toString()
        if ("" == newPassword) { //密码为空
            Toast.makeText(activity, "新密码为空", Toast.LENGTH_LONG).show()
            return
        }
        val userId = SPUtils.get(this@PasswordActivity, SPUtils.USER_ID, 0) as Int
        db.execSQL("update user set password = ? where id = ?", arrayOf<Any>(userId))
        Toast.makeText(this@PasswordActivity, "更新成功", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun back(view: View?) {
        finish()
    }
}