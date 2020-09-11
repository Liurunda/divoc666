package com.java.liurunda;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liurunda.data.InfoType;
import com.java.liurunda.data.News;
import com.java.liurunda.data.NewsGetter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SearchFragment extends Fragment {
    private View view;
    private ArrayList<News> newsList = new ArrayList<>();
    private NewsGetter getter;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;
    private NewsRollAdapter adapter;
    private SearchFragment() {

    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }


    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v: viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view = inflater.inflate(R.layout.fragment_search, container, false);

        recycler = this.view.findViewById(R.id.searchRoll);

        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        newsList.add(new News());
        adapter = new NewsRollAdapter(newsList);
        recycler.setAdapter(adapter);

        BottomNavigationView nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav);

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
        SearchView bar = view.findViewById(R.id.newsSearch);
        ArrayList<View> searchList = new ArrayList<>();
        searchList.add(bar);
        //返回搜索结果
        getter = NewsGetter.Getter();
        bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                CompletableFuture.supplyAsync(()->getter.search_result(s))
                        .thenAccept((list) -> {
                            newsList.clear();
                            newsList.addAll(list);
                            getActivity().runOnUiThread(adapter::notifyDataSetChanged);
                        });
                hideSoftKeyboard(getContext(),searchList);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        //显示历史记录

        return this.view;
    }
    Fragment[] Current;
    public void passCurrent(Fragment[] f_current) {
        Current = f_current;
    }
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            Current[0]=this;
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Toast t = new Toast(getContext());
//        t.setText("66666");
//        t.show();
    }
}
