package com.java.liurunda;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class NewsRollAdapter extends RecyclerView.Adapter<NewsRollAdapter.NewsListViewHolder> {
    private String[] news_set;

    public static class NewsListViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout layout;
        public NewsListViewHolder(FrameLayout l) {
            super(l);
            layout = l;
        }
    }

    public NewsRollAdapter(String[] news_set) {
        this.news_set = news_set;
//        System.out.println(Arrays.toString(news_set));
    }

    @NotNull
    @Override
    public NewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_entry, parent, false);
        return new NewsListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsListViewHolder holder, int position) {
//        holder.textView.setText(news_set[position]);
        TextView v = holder.layout.findViewById(R.id.entry_view);
        v.setText(news_set[position]);
//        System.out.println(position + " " + news_set[position]);
    }

    @Override
    public int getItemCount() {
        System.out.println(news_set.length);
        return news_set.length;
    }
}
