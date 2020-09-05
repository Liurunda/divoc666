package com.java.liurunda;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.News;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_entry, parent, false);
        return new NewsListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsListViewHolder holder, int position) {
        News news = news_set.get(position);
        TextView v = holder.layout.findViewById(R.id.view_title);
        v.setText(news.title);
        TextView vt = holder.layout.findViewById(R.id.view_time);
        vt.setText(news.datetime);
        TextView vs = holder.layout.findViewById(R.id.view_source);
        vs.setText(news.source);
        System.out.println("BindViewHolder: " + position);
    }

    @Override
    public int getItemCount() {
        return news_set.size();
    }
}
