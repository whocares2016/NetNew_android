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
import com.example.netnew.bean.User
import com.example.netnew.util.SPUtils

/**
 * 登录页面
 */
class LoginActivity : AppCompatActivity() {
    var helper: MySqliteOpenHelper? = null
    private var etAccount //账号
            : EditText? = null
    private var etPassword //密码
            : EditText? = null
    private var tvRegister //注册
            : TextView? = null
    private var btnLogin //登录按钮
            : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        helper = MySqliteOpenHelper(this)
        etAccount = findViewById(R.id.et_account) //获取手机号
        etPassword = findViewById(R.id.et_password) //获取密码
        tvRegister = findViewById(R.id.tv_register) //获取注册
        btnLogin = findViewById(R.id.btn_login) //获取登录
        //手机号注册
        tvRegister!!.setOnClickListener(View.OnClickListener { //跳转到注册页面
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        })

        //设置点击按钮
        btnLogin!!.setOnClickListener(View.OnClickListener {
            //获取请求参数
            val account = etAccount!!.getText().toString()
            val password = etPassword!!.getText().toString()
            if ("" == account) { //用户名不能为空
                Toast.makeText(this@LoginActivity, "用户名不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == password) { //密码为空
                Toast.makeText(this@LoginActivity, "密码不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            var mUser: User? = null
            //通过账号查询用户是否存在
            val sql = "select * from user where account = ?"
            val db = helper!!.writableDatabase
            val cursor = db.rawQuery(sql, arrayOf(account))
            if (cursor != null && cursor.columnCount > 0) {
                while (cursor.moveToNext()) {
                    val dbId = cursor.getInt(0)
                    val dbAccount = cursor.getString(1)
                    val dbPassword = cursor.getString(2)
                    val dbName = cursor.getString(3)
                    val dbSex = cursor.getString(4)
                    val dbPhone = cursor.getString(5)
                    val dbAddress = cursor.getString(6)
                    val dbPhoto = cursor.getString(7)
                    mUser = User(
                        dbId,
                        dbAccount,
                        dbPassword,
                        dbName,
                        dbSex,
                        dbPhone,
                        dbAddress,
                        dbPhoto
                    )
                }
            }
            db.close()
            if (mUser != null) { //用户存在
                if (password != mUser.password) { //判断密码是否正确
                    Toast.makeText(this@LoginActivity, "密码错误", Toast.LENGTH_SHORT).show()
                } else { //密码验证成功
                    SPUtils.put(this@LoginActivity, SPUtils.USER_ID, mUser.id)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else { //账号不存在
                Toast.makeText(this@LoginActivity, "账号不存在", Toast.LENGTH_SHORT).show()
            }
        })
    }
}