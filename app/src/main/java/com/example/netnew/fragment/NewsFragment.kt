package com.example.netnew.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Context
import com.google.android.material.tabs.TabLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.netnew.adapter.NewsDataAdapter
import com.google.gson.GsonBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.netnew.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.example.netnew.util.OkHttpTool
import com.example.netnew.bean.NewsTitle
import org.json.JSONObject
import com.google.gson.reflect.TypeToken
import com.example.netnew.bean.NewsDataBean
import org.json.JSONException
import android.view.View
import java.lang.Exception
import java.util.ArrayList
import java.util.HashMap

/**
 * 新闻
 */
class NewsFragment : Fragment() {
    private var myActivity //上下文
            : Activity? = null
    private var tabTitle: TabLayout? = null
    private var rvNewsList: RecyclerView? = null
    private var mNewsAdapter: NewsDataAdapter? = null
    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
    private var type: String? = null
    private var titles: MutableList<String>? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        tabTitle = view.findViewById(R.id.tab_title)
        rvNewsList = view.findViewById(R.id.rv_news_list)
        initView()
        return view
    }

    /**
     * 初始化页面
     */
    private fun initView() {
        loadTitle()
        val layoutManager = LinearLayoutManager(myActivity)
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3、设置recyclerView的布局管理器
        rvNewsList!!.layoutManager = layoutManager
        //==2、实例化适配器
        //=2.1、初始化适配器
        mNewsAdapter = NewsDataAdapter()
        //=2.3、设置recyclerView的适配器
        rvNewsList!!.adapter = mNewsAdapter
        tabTitle!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                type = titles!![tab.position]
                loadData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun loadTitle() {
        titles = ArrayList()
        val url = "https://way.jd.com/jisuapi/channel"
        val map: MutableMap<String, Any> = HashMap()
        map["appkey"] = "95b2cfcb40f32439e93af7c77ec2ac6b"
        OkHttpTool.httpGet(url, map) {isSuccess: Boolean, responseCode: Int, response: String, exception:Exception ->
            myActivity!!.runOnUiThread {
                if (isSuccess && responseCode == 200) {
                    val newsTitle = gson.fromJson(response, NewsTitle::class.java)
                    if ("10000" == newsTitle.code) {
                        titles!!.addAll(newsTitle.result!!.result!!)
                        tabTitle!!.tabMode = TabLayout.MODE_SCROLLABLE
                        //为TabLayout添加tab名称
                        for (i in titles!!.indices) {
                            tabTitle!!.addTab(tabTitle!!.newTab().setText(titles!!.get(i)))
                        }
                        type = titles!!.get(0)
                        loadData()
                    }
                }
            }
        }
    }

    private fun loadData() {
        val url = "https://way.jd.com/jisuapi/get"
        val map: MutableMap<String, Any?> = HashMap()
        map["channel"] = type
        map["num"] = "40"
        map["start"] = "0"
        map["appkey"] = "95b2cfcb40f32439e93af7c77ec2ac6b"
        OkHttpTool.httpGet(url, map) { isSuccess: Boolean, responseCode:Int, response:String, exception:Exception ->
            myActivity!!.runOnUiThread {
                if (isSuccess && responseCode == 200) {
                    try {
                        val jsonObject = JSONObject(response)
                        val result = jsonObject.getString("result")
                        val jsonResult = JSONObject(result)
                        val result2 = jsonResult.getString("result")
                        val jsonResult2 = JSONObject(result2)
                        val list = jsonResult2.getString("list")
                        val type = object : TypeToken<List<NewsDataBean?>?>() {}.type //列表信息
                        val newsDataBeanList = gson.fromJson<List<NewsDataBean>>(list, type)
                        if (newsDataBeanList.size > 0) {
                            mNewsAdapter!!.addItem(newsDataBeanList)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}