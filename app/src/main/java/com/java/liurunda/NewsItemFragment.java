package com.java.liurunda;

import android.os.Bundle;
import android.widget.Adapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.News;
import com.java.liurunda.data.NewsGetter;

import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsItemFragment extends Fragment {
    private static final String ARG_TYPE = "category";
    private String categoryName;
    private View view;

    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;
    private NewsRollAdapter adapter;

    public NewsItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryName Category of the news displayed.
     * @return A new instance of fragment NewsItemFragment.
     */
    public static NewsItemFragment newInstance(String categoryName) {
        NewsItemFragment fragment = new NewsItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_TYPE);
        }

        NewsGetter getter = new NewsGetter();
        CompletableFuture.supplyAsync(getter::initial_news).thenAccept(
            (list)-> {
                System.out.println(list.size());
                for (News n: list) {
                    System.out.println(n);
                }
            }
        );
        System.out.println("here is it");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_news_item, container, false);

//        TextView text_view = this.view.findViewById(R.id.text_view);
//        text_view.setText("Welcome to " + categoryName);

        recycler = this.view.findViewById(R.id.newsRoll);

        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        String[] news_set = {"hello", "tab 2", "tab 3", "tab 4", "tab 5", categoryName};

        adapter = new NewsRollAdapter(news_set);
        recycler.setAdapter(adapter);

        return this.view;
    }
}