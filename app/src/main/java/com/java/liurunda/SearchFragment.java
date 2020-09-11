package com.java.liurunda;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import java.util.zip.Inflater;
//https://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position


public class SearchFragment extends Fragment {
    private View view;
    private ArrayList<News> newsList = new ArrayList<>();
    private NewsGetter getter;
    private RecyclerView recycler;
    private NpaLinearLayoutManager layoutManager;
    private SearchRollAdapter adapter;
    private ListPopupWindow hint;
    private ArrayList<String> hints = new ArrayList<>();
    private ArrayAdapter hintAdapter;
    private static class NpaLinearLayoutManager extends LinearLayoutManager {
        public NpaLinearLayoutManager(Context context) {
            super(context);
        }

        public NpaLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public NpaLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        /**
         * Disable predictive animations. There is a bug in RecyclerView which causes views that
         * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
         * adapter size has decreased since the ViewHolder was recycled.
         */
        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }
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

        layoutManager = new NpaLinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);

        adapter = new SearchRollAdapter(newsList);
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
        bar.setIconifiedByDefault(false);
        bar.setIconified(false);
        ArrayList<View> searchList = new ArrayList<>();
        searchList.add(bar);
        //返回搜索结果
        getter = NewsGetter.Getter();

        bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                hint.dismiss();
                CompletableFuture.supplyAsync(()->getter.search_result(s))
                        .thenAccept((list) -> {
                            newsList.clear();
                           // adapter.notifyDataSetChanged();
                            getActivity().runOnUiThread(adapter::notifyDataSetChanged);
                            newsList.addAll(list);
                            //adapter.notifyDataSetChanged();
                            getActivity().runOnUiThread(adapter::notifyDataSetChanged);
                        });
                hideSoftKeyboard(getContext(),searchList);
                hints.remove(s);
                hints.add(0,s);
                hintAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                hint.show();
                return false;
            }
        });
        //显示历史记录
        hint = new ListPopupWindow(view.getContext());
        hint.setAnchorView(bar);
        hint.setHeight(700);
        hint.setModal(false);
        hintAdapter = new ArrayAdapter(view.getContext(),R.layout.one_line_hint,hints);
        hint.setAdapter(hintAdapter);
        CompletableFuture.runAsync(()->getter.load_history(hints)).thenRun(
                hintAdapter::notifyDataSetChanged
        );

        bar.clearFocus();
        bar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hint.show();
            }
        });
        bar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hint.show();
            }
        });

        hint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bar.setQuery(hints.get(i),false);
            }
        });

        return this.view;
    }
    Fragment[] Current;
    public void passCurrent(Fragment[] f_current) {
        Current = f_current;
    }
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            hint.dismiss();
            getter.save_history(hints);
        }
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
    @Override
    public void onPause(){
        hint.dismiss();
        super.onPause();
        getter.save_history(hints);
    }
}
