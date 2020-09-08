package com.java.liurunda;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.java.liurunda.data.InfoType;
import com.java.liurunda.data.News;
import com.java.liurunda.data.NewsGetter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsItemFragment extends Fragment {
    private static final String ARG_TYPE_1 = "category";
    private static final String ARG_TYPE_2 = "info_type";
    private String categoryName;
    private InfoType infoType;
    private View view;
    private ArrayList<News> newsList;
    private NewsGetter getter;

    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;
    private NewsRollAdapter adapter;

    public NewsItemFragment() {
        this.newsList = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryName Category of the news displayed.
     * @return A new instance of fragment NewsItemFragment.
     */
    public static NewsItemFragment newInstance(String categoryName, InfoType infoType) {
        NewsItemFragment fragment = new NewsItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE_1, categoryName);
        args.putInt(ARG_TYPE_2, infoType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_TYPE_1);
            infoType = InfoType.values()[getArguments().getInt(ARG_TYPE_2)];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_news_item, container, false);

        recycler = this.view.findViewById(R.id.newsRoll);

        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        adapter = new NewsRollAdapter(newsList);
        recycler.setAdapter(adapter);

        BottomNavigationView nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav);
//        RecyclerView recycler = this.view.findViewById(R.id.newsRoll);

        final Boolean[] isBottomShow = {true};

        recycler.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY - oldScrollY > 0 && isBottomShow[0]) {
                isBottomShow[0] = false;
                nav.animate().translationY(nav.getHeight());
            } else if (scrollY - oldScrollY < 0 && !isBottomShow[0]) {
                isBottomShow[0] = true;
                nav.animate().translationY(0);
            }
        });

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    adapter.changeMoreStatus(NewsRollAdapter.LOADING_MORE);
                    CompletableFuture.supplyAsync(() -> {
                        final int countNews = 10;
                        return getter.older_news(infoType, countNews);
                    }).thenAccept((ArrayList<News> news) -> {
                        final int rangeStart = newsList.size();
                        newsList.addAll(news);
                        adapter.notifyItemRangeInserted(rangeStart, news.size());
                        showSnackbar(getString(R.string.text_refresh_success));
                    });
                }
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        getter = NewsGetter.Getter();
        CompletableFuture.supplyAsync(() -> getter.initial_news(infoType)).thenAccept((list) -> {
            newsList.addAll(list);
            getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        });

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefresher);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> CompletableFuture.supplyAsync(() -> getter.latest_news(infoType)).thenAccept((list) -> {
            newsList.addAll(0, list);
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> adapter.notifyDataSetChanged());
            swipeRefreshLayout.setRefreshing(false);
            showSnackbar(getString(R.string.text_refresh_success));
        }));

        return this.view;
    }

    void showSnackbar(final String message) {
        Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.layout), message, Snackbar.LENGTH_SHORT);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snack.getView().getLayoutParams();
        params.setAnchorId(R.id.bottom_nav);
        params.anchorGravity = Gravity.TOP;
        params.gravity = Gravity.TOP;
        snack.getView().setLayoutParams(params);
        snack.show();
    }
}