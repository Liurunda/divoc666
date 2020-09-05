package com.java.liurunda;

import android.os.Bundle;
import android.widget.Adapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.java.liurunda.data.News;
import com.java.liurunda.data.NewsGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment {
    private View view;
    private ArrayList<Fragment> fragments;
    private final int[] category_id = {R.string.category_all, R.string.category_event, R.string.category_points, R.string.category_news, R.string.category_paper};

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsListFragment.
     */
    public static NewsListFragment newInstance() {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_news_list, container, false);
        TabLayout tabs = this.view.findViewById(R.id.tabs);
        ViewPager view_pager = this.view.findViewById(R.id.view_pager);
        fragments = new ArrayList<Fragment>();
        fragments.add(NewsItemFragment.newInstance(getString(category_id[0])));
        fragments.add(NewsItemFragment.newInstance(getString(category_id[1])));
        fragments.add(NewsItemFragment.newInstance(getString(category_id[2])));
        fragments.add(NewsItemFragment.newInstance(getString(category_id[3])));
        fragments.add(NewsItemFragment.newInstance(getString(category_id[4])));

        NewsItemAdapter adapter = new NewsItemAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragments);
        view_pager.setAdapter(adapter);
        tabs.setupWithViewPager(view_pager);
        return this.view;
    }

    public class NewsItemAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public NewsItemAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(category_id[position]);
        }
    }
}