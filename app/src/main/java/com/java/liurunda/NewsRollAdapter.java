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
    public static final int PULLUP_LOAD_MORE = 0; //上拉加载更多
    public static final int LOADING_MORE     = 1; //正在加载
    public static final int NO_LOAD_MORE     = 2; //没有加载更多 隐藏
    private int mLoadMoreStatus = 0; //上拉加载更多状态-默认为0

    public NewsRollAdapter(ArrayList<News> news_set) {
        this.news_set = news_set;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            NewsListViewHolder v = new NewsListViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_entry, parent, false));
            return v;
        } else {
            assert(viewType == TYPE_FOOTER);
            FooterViewHolder v = new FooterViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_loader, parent, false));
            return v;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsListViewHolder) {
            NewsListViewHolder hold = (NewsListViewHolder) holder;
            News news = news_set.get(position);
            TextView v = hold.layout.findViewById(R.id.view_title);
            v.setText(news.title);
            if (news.haveread != 0) {
                v.setTextColor(R.color.text_Gray);
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
            hold.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = (News) v.getTag();
                    CompletableFuture.runAsync(() -> {
                        NewsGetter.Getter().markNewsRead(news);
                        notifyItemChanged(position);
                    });
                    if (news.infoType == InfoType.news.ordinal() || news.infoType == InfoType.paper.ordinal()) {
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(), NewsDetailActivity.class);
                        intent.putExtra("news_content", news);
                        v.getContext().startActivity(intent);
                    }
                }
            });
            hold.itemView.setTag(news);
        } else {
//            FooterViewHolder hold = (FooterViewHolder) holder;
//            switch (mLoadMoreStatus) {
//                case PULLUP_LOAD_MORE:
//                    hold.mTvLoadText.setText("上拉加载更多...");
//                    break;
//                case LOADING_MORE:
//                    hold.mTvLoadText.setText("正加载更多...");
//                    break;
//                case NO_LOAD_MORE:
//                    hold.mLoadLayout.setVisibility(View.GONE);
//                    break;
//            }
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

    public void changeMoreStatus(int status){
        mLoadMoreStatus = status;
        notifyDataSetChanged();
    }
}
