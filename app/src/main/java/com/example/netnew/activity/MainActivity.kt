package com.example.netnew.activity

import android.app.Activity
import android.app.Fragment
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
import android.view.KeyEvent
import android.view.View

/**
 * 主页面
 */
class MainActivity : Activity() {
    private var myActivity: Activity? = null
    private var llContent: LinearLayout? = null
    private var tvTitle: TextView? = null
    private var rbNews: RadioButton? = null
    private var rbUser: RadioButton? = null
    private var mNetReceiver: NetworkReceiver? = null
    private val fragments = arrayOf<Fragment?>(null, null) //存放Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myActivity = this
        setContentView(R.layout.activity_main)
        MyApplication.Instance!!.mainActivity = myActivity
        llContent = findViewById<View>(R.id.ll_main_content) as LinearLayout
        tvTitle = findViewById<View>(R.id.title) as TextView
        rbNews = findViewById<View>(R.id.rb_main_news) as RadioButton
        rbUser = findViewById<View>(R.id.rb_main_user) as RadioButton
        initView()
        setViewListener()
        initNetworkReceiver()
    }

    private fun initNetworkReceiver() {
        mNetReceiver = NetworkReceiver()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(mNetReceiver, filter)
    }

    private fun setViewListener() {
        rbNews!!.setOnClickListener {
            tvTitle!!.text = "新闻"
            switchFragment(0)
        }
        rbUser!!.setOnClickListener {
            tvTitle!!.text = "我的"
            switchFragment(1)
        }
    }

    private fun initView() {
        //设置导航栏图标样式
        val iconNews = resources.getDrawable(R.drawable.selector_main_rb_news) //设置主页图标样式
        iconNews.setBounds(0, 0, 68, 68) //设置图标边距 大小
        rbNews!!.setCompoundDrawables(null, iconNews, null, null) //设置图标位置
        rbNews!!.compoundDrawablePadding = 5 //设置文字与图片的间距
        val iconUser = resources.getDrawable(R.drawable.selector_main_rb_user) //设置主页图标样式
        iconUser.setBounds(0, 0, 55, 55) //设置图标边距 大小
        rbUser!!.setCompoundDrawables(null, iconUser, null, null) //设置图标位置
        rbUser!!.compoundDrawablePadding = 5 //设置文字与图片的间距
        switchFragment(0)
        rbNews!!.isChecked = true
    }

    /**
     * 方法 - 切换Fragment
     *
     * @param fragmentIndex 要显示Fragment的索引
     */
    private fun switchFragment(fragmentIndex: Int) {
        //在Activity中显示Fragment
        //1、获取Fragment管理器 FragmentManager
        val fragmentManager = this.fragmentManager
        //2、开启fragment事务
        val transaction = fragmentManager.beginTransaction()

        //懒加载 - 如果需要显示的Fragment为null，就new。并添加到Fragment事务中
        if (fragments[fragmentIndex] == null) {
            when (fragmentIndex) {
                0 -> fragments[fragmentIndex] = NewsFragment()
                1 -> fragments[fragmentIndex] = UserFragment()
            }

            //==添加Fragment对象到Fragment事务中
            //参数：显示Fragment的容器的ID，Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex])
        }

        //隐藏其他的Fragment
        for (i in fragments.indices) {
            if (fragmentIndex != i && fragments[i] != null) {
                //隐藏指定的Fragment
                transaction.hide(fragments[i])
            }
        }
        //4、显示Fragment
        transaction.show(fragments[fragmentIndex])

        //5、提交事务
        transaction.commit()
    }

    /**
     * 双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
        }
        return false
    }

    private var time: Long = 0
    fun exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(myActivity, "再点击一次退出应用程序", Toast.LENGTH_LONG).show()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mNetReceiver)
    }
}