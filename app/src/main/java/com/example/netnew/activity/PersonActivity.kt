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
 * 个人信息
 */
class PersonActivity : AppCompatActivity() {
    var helper: MySqliteOpenHelper? = null
    private var mActivity: Activity? = null
    private var tvAccount: TextView? = null
    private var etNickName: EditText? = null
    private var etPhone //手机号
            : EditText? = null
    private var etAddress //地址
            : EditText? = null
    private var rgSex //性别
            : RadioGroup? = null
    private var btnSave //保存
            : Button? = null
    var mUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)
        mActivity = this
        //val temp= mActivity
        helper = MySqliteOpenHelper(mActivity!!)
        tvAccount = findViewById(R.id.tv_account)
        etNickName = findViewById(R.id.et_nickName) //获取昵称
        etPhone = findViewById(R.id.et_phone) //获取手机号
        etAddress = findViewById(R.id.et_address) //获取地址
        rgSex = findViewById(R.id.rg_sex)
        btnSave = findViewById(R.id.btn_save)
        initView()
    }

    private fun initView() {
        val userId = SPUtils.get(mActivity!!, SPUtils.USER_ID, 0) as Int
        val db = helper!!.writableDatabase
        val sql = "select * from user where id = ?"
        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))
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
                tvAccount!!.text = dbAccount
                etNickName!!.setText(dbName)
                etPhone!!.setText(dbPhone)
                etAddress!!.setText(dbAddress)
                rgSex!!.check(if ("男" == dbSex) R.id.rb_man else R.id.rb_woman)
                mUser =
                    User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhone, dbAddress, dbPhoto)
            }
        }
        db.close()

        //保存
        btnSave!!.setOnClickListener(View.OnClickListener {
            val db = helper!!.writableDatabase
            val nickName = etNickName!!.text.toString()
            val phone = etPhone!!.text.toString()
            val address = etAddress!!.text.toString()
            if ("" == nickName) { //昵称不能为空
                Toast.makeText(mActivity, "昵称不能为空", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if ("" == phone) { //手机号不能为空
                Toast.makeText(mActivity, "手机号不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (phone.length != 11) { //手机号格式错误
                Toast.makeText(mActivity, "手机号格式错误", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == address) { //地址不能为空
                Toast.makeText(mActivity, "地址不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            val sex = if (rgSex!!.checkedRadioButtonId == R.id.rb_man) "男" else "女" //性别
            db.execSQL(
                "update user set name = ?,phone=?,address=?,sex=? where id = ?",
                arrayOf<Any>(nickName, phone, address, sex, mUser!!.id)
            )
            Toast.makeText(this@PersonActivity, "更新成功", Toast.LENGTH_SHORT).show()
            db.close()
            finish()
        })
    }

    fun back(view: View?) {
        finish()
    }
}