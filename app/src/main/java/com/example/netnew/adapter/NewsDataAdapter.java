package com.example.netnew.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netnew.R;
import com.example.netnew.activity.NewsDataDetailActivity;
import com.example.netnew.bean.NewsDataBean;

import java.util.ArrayList;
import java.util.List;

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.ViewHolder> {
    private List<NewsDataBean> list =new ArrayList<>();
    private Context mActivity;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_new_data_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NewsDataBean newsDataBean = list.get(i);
        if (newsDataBean != null) {
            viewHolder.title.setText(newsDataBean.getTitle());
            viewHolder.author_name.setText(newsDataBean.getSrc());
            viewHolder.date.setText(newsDataBean.getTime());
            Glide.with(mActivity).load(newsDataBean.getPic()).into(viewHolder.thumbnail_pic_s);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, NewsDataDetailActivity.class);
                    intent.putExtra("url",newsDataBean.getUrl());
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<NewsDataBean> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author_name;
        private TextView date;
        private ImageView thumbnail_pic_s;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author_name = itemView.findViewById(R.id.author_name);
            date = itemView.findViewById(R.id.date);
            thumbnail_pic_s = itemView.findViewById(R.id.thumbnail_pic_s);
        }
    }
}
