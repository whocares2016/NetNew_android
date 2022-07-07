package com.example.netnew.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netnew.R;
import com.example.netnew.adapter.NewsDataAdapter;
import com.example.netnew.bean.NewsDataBean;
import com.example.netnew.bean.NewsTitle;
import com.example.netnew.util.OkHttpTool;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新闻
 */

public class NewsFragment extends Fragment {
    private Activity myActivity;//上下文
    private TabLayout tabTitle;
    private RecyclerView rvNewsList;
    private NewsDataAdapter mNewsAdapter;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private String type;
    private List<String> titles = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        tabTitle = view.findViewById(R.id.tab_title);
        rvNewsList = view.findViewById(R.id.rv_news_list);
        initView();
        return view;
    }

    /**
     * 初始化页面
     */
    private void initView() {
        loadTitle();
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvNewsList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        mNewsAdapter = new NewsDataAdapter();
        //=2.3、设置recyclerView的适配器
        rvNewsList.setAdapter(mNewsAdapter);
        tabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                type = titles.get(tab.getPosition());
                loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadTitle() {
        titles = new ArrayList<>();
        String url = "https://way.jd.com/jisuapi/channel";
        Map<String, Object> map = new HashMap<>();
        map.put("appkey", "95b2cfcb40f32439e93af7c77ec2ac6b");
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            NewsTitle newsTitle = gson.fromJson(response, NewsTitle.class);
                            if ("10000".equals(newsTitle.getCode())) {
                                titles.addAll(newsTitle.getResult().getResult());
                                tabTitle.setTabMode(TabLayout.MODE_SCROLLABLE);
                                //为TabLayout添加tab名称
                                for (int i = 0; i < titles.size(); i++) {
                                    tabTitle.addTab(tabTitle.newTab().setText(titles.get(i)));
                                }
                                type = titles.get(0);
                                loadData();
                            }

                        }
                    }
                });
            }
        });
    }

    private void loadData() {
        String url = "https://way.jd.com/jisuapi/get";
        Map<String, Object> map = new HashMap<>();
        map.put("channel", type);
        map.put("num", "40");
        map.put("start", "0");
        map.put("appkey", "95b2cfcb40f32439e93af7c77ec2ac6b");
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString("result");
                                JSONObject jsonResult = new JSONObject(result);
                                String result2 = jsonResult.getString("result");
                                JSONObject jsonResult2 = new JSONObject(result2);
                                String list = jsonResult2.getString("list");
                                Type type = new TypeToken<List<NewsDataBean>>() {
                                }.getType();//列表信息
                                List<NewsDataBean> newsDataBeanList = gson.fromJson(list, type);
                                if (newsDataBeanList.size() > 0) {
                                    mNewsAdapter.addItem(newsDataBeanList);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

}
