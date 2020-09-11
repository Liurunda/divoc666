package com.java.liurunda;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.InfoType;
import com.java.liurunda.data.News;
import com.java.liurunda.data.NewsGetter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class NewsRollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<News> news_set;
    private static final int TYPE_ITEM   = 0;
    private static final int TYPE_FOOTER = 1;

    public NewsRollAdapter(ArrayList<News> news_set) {
        this.news_set = news_set;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new NewsListViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_entry, parent, false));
        } else {
            return new FooterViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_loader, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsListViewHolder) {
            NewsListViewHolder hold = (NewsListViewHolder) holder;
            News news = news_set.get(position);
            TextView v = hold.layout.findViewById(R.id.view_title);
            v.setText(news.title);
            if (news.haveread != 0) {
                v.setTextColor(R.color.design_default_color_on_secondary);
            }
            LinearLayout meta = hold.layout.findViewById(R.id.layout_meta);
            if ((news.datetime == null || news.datetime.equals("")) && (news.source == null || news.source.equals(""))) {
                meta.setVisibility(View.GONE);
            } else {
                TextView vt = hold.layout.findViewById(R.id.view_time);
                vt.setText(DateUtil.getTextFormattedDate(news.datetime));
                TextView vs = hold.layout.findViewById(R.id.view_source);
                vs.setText(news.source);
            }
            hold.itemView.setOnClickListener(v1 -> {
                News news1 = (News) v1.getTag();
                if (news1.infoType == InfoType.news.ordinal() || news1.infoType == InfoType.paper.ordinal()) {
                    NewsGetter.Getter().markNewsRead(news1);
                    news1.haveread = 1;
                    notifyItemChanged(position);

                    Intent intent = new Intent();
                    intent.setClass(v1.getContext(), NewsDetailActivity.class);
                    intent.putExtra("news_content", news1);
                    v1.getContext().startActivity(intent);
                }
            });
            hold.itemView.setTag(news);
            if (news.infoType == InfoType.event.ordinal() || news.infoType == InfoType.points.ordinal()) {
                hold.itemView.setBackground(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return news_set.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public static class NewsListViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout layout;
        public NewsListViewHolder(FrameLayout l) {
            super(l);
            layout = l;
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
