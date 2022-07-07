package com.example.netnew.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.netnew.R
import com.example.netnew.util.MySqliteOpenHelper
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import android.content.Intent
import android.net.Uri
import com.example.netnew.activity.PersonActivity
import com.example.netnew.activity.PasswordActivity
import com.example.netnew.activity.VideoActivity
import com.example.netnew.activity.LoginActivity
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.example.netnew.util.GlideEngine
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.entity.LocalMedia
import android.text.TextUtils
import android.os.Build
import android.view.View
import android.widget.*
import com.example.netnew.bean.User
import com.example.netnew.util.SPUtils
import com.luck.picture.lib.tools.PictureFileUtils

/**
 * 我的
 */
class UserFragment : Fragment() {
    var helper: MySqliteOpenHelper? = null
    private var mActivity: Activity? = null
    private var ivPhoto: ImageView? = null
    private var tvNickName: TextView? = null
    private var llPerson: LinearLayout? = null
    private var llSecurity: LinearLayout? = null
    private var llVideo: LinearLayout? = null
    private var btnLogout: Button? = null
    private var imagePath = ""
    private var mUser: User? = null
    private val headerRO = RequestOptions().circleCrop() //圆角变换
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        helper = MySqliteOpenHelper(mActivity!!)
        ivPhoto = view.findViewById(R.id.iv_photo)
        tvNickName = view.findViewById(R.id.tv_nickName)
        llPerson = view.findViewById(R.id.person)
        llSecurity = view.findViewById(R.id.security)
        llVideo = view.findViewById(R.id.video)
        btnLogout = view.findViewById(R.id.logout)
        initData()
        initView()
        return view
    }

    /**
     * 初始化数据
     */
    private fun initData() {
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
                mUser =
                    User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhone, dbAddress, dbPhoto)
            }
        }
        db.close()
        tvNickName!!.text = mUser!!.name
        Glide.with(mActivity!!)
            .load(mUser!!.photo)
            .apply(headerRO.error(if ("男" == mUser!!.sex) R.drawable.ic_default_man else R.drawable.ic_default_woman))
            .into(ivPhoto!!)
    }

    private fun initView() {
        //从相册中选择头像
        ivPhoto!!.setOnClickListener { selectClick() }
        //个人信息
        llPerson!!.setOnClickListener { //跳转页面
            val intent = Intent(mActivity, PersonActivity::class.java)
            startActivity(intent)
        }
        //账号安全
        llSecurity!!.setOnClickListener { //跳转页面
            val intent = Intent(mActivity, PasswordActivity::class.java)
            startActivity(intent)
        }
        //视频介绍
        llVideo!!.setOnClickListener { //跳转页面
            val intent = Intent(mActivity, VideoActivity::class.java)
            startActivity(intent)
        }
        //退出登录
        btnLogout!!.setOnClickListener {
            SPUtils.remove(mActivity!!, SPUtils.USER_ID)
            startActivity(Intent(mActivity, LoginActivity::class.java))
            mActivity!!.finish()
        }
    }

    /**
     * 选择图片
     */
    private fun selectClick() {
        val db = helper!!.writableDatabase
        PictureSelector.create(mActivity)
            .openGallery(PictureMimeType.ofAll())
            .imageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(1)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: List<LocalMedia>) {
                    for (i in result.indices) {
                        // onResult Callback
                        val media = result[i]
                        var path: String
                        // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                        val compressPath = media.isCompressed || media.isCut && media.isCompressed
                        // 裁剪过
                        val isCutPath = media.isCut && !media.isCompressed
                        path = if (isCutPath) {
                            media.cutPath
                        } else if (compressPath) {
                            media.compressPath
                        } else if (!TextUtils.isEmpty(media.androidQToPath)) {
                            // AndroidQ特有path
                            media.androidQToPath
                        } else if (!TextUtils.isEmpty(media.realPath)) {
                            // 原图
                            media.realPath
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                PictureFileUtils.getPath(mActivity, Uri.parse(media.path))
                            } else {
                                media.path
                            }
                        }
                        imagePath = path
                    }
                    Glide.with(mActivity!!)
                        .load(imagePath)
                        .apply(headerRO.error(if ("男" == mUser!!.sex) R.drawable.ic_default_man else R.drawable.ic_default_woman))
                        .into(ivPhoto!!)
                    db.execSQL(
                        "update user set photo = ? where id = ?",
                        arrayOf<Any>(imagePath, mUser!!.id)
                    )
                    Toast.makeText(mActivity, "更新成功", Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {
                    // onCancel Callback
                }
            })
    }
}