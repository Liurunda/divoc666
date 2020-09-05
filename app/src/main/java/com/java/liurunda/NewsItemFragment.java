package com.java.liurunda;

import android.os.Bundle;
import android.widget.Adapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.InfoType;
import com.java.liurunda.data.News;
import com.java.liurunda.data.NewsGetter;

import java.util.ArrayList;
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

    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;
    private NewsRollAdapter adapter;

    public NewsItemFragment() {
        this.newsList = new ArrayList<>();
        // Required empty public constructor
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
//        System.out.println("setAdapter: " + System.identityHashCode(newsList));
        recycler.setAdapter(adapter);

        NewsGetter getter = new NewsGetter();
        CompletableFuture.supplyAsync(getter::initial_news).thenAccept(
                (list) -> {
                    newsList.addAll(list);
//                    System.out.println(list.toString()
//                    System.out.println("========== News fetched successfully ==========");
                    getActivity().runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
//                    System.out.println("Update newsList: " + System.identityHashCode(newsList));
//                    System.out.println(newsList.toString());
                }
        );

        return this.view;
    }
}