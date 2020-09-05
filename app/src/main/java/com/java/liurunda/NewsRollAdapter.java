package com.java.liurunda;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.InfoType;
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
        TextView v = holder.layout.findViewById(R.id.entry_view);
        v.setText(news_set.get(position).title);
        System.out.println("BindViewHolder: " + position);
    }

    @Override
    public int getItemCount() {
        System.out.println(news_set.size() + " ========== getItemCount()");
        return news_set.size();
    }
}
