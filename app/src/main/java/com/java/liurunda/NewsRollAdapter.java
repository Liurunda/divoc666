package com.java.liurunda;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.InfoType;
import com.java.liurunda.data.News;
import com.java.liurunda.DateUtil;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class NewsRollAdapter extends RecyclerView.Adapter<NewsRollAdapter.NewsListViewHolder> {
    private ArrayList<News> news_set;

    public static class NewsListViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout layout;
        public NewsListViewHolder(FrameLayout l) {
            super(l);
            layout = l;
        }
    }

    public NewsRollAdapter(ArrayList<News> news_set) {
        this.news_set = news_set;
    }

    @NotNull
    @Override
    public NewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewsListViewHolder v = new NewsListViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_entry, parent, false));
        v.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News news = (News) v.getTag();
                if (news.infoType == InfoType.news.ordinal() || news.infoType == InfoType.paper.ordinal()) {
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), NewsDetailActivity.class);
                    intent.putExtra("news_content", news);
                    v.getContext().startActivity(intent);
                }
            }
        });
        return v;
    }

    @Override
    public void onBindViewHolder(NewsListViewHolder holder, int position) {
        News news = news_set.get(position);
        TextView v = holder.layout.findViewById(R.id.view_title);
        v.setText(news.title);
        LinearLayout meta = holder.layout.findViewById(R.id.layout_meta);
        if ((news.datetime == null || news.datetime.equals("")) && (news.source == null || news.source.equals(""))) {
            meta.setVisibility(View.GONE);
        } else {
            TextView vt = holder.layout.findViewById(R.id.view_time);
            vt.setText(DateUtil.getTextFormattedDate(news.datetime));
            TextView vs = holder.layout.findViewById(R.id.view_source);
            vs.setText(news.source);
        }
        holder.itemView.setTag(news);
    }

    @Override
    public int getItemCount() {
        return news_set.size();
    }
}
