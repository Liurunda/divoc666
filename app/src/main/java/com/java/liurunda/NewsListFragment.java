package com.java.liurunda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.java.liurunda.data.InfoType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        view_pager.setOffscreenPageLimit(5);

        NewsItemFragment[] subfragments = {
            NewsItemFragment.newInstance(getString(category_id[0]), InfoType.all),
            NewsItemFragment.newInstance(getString(category_id[1]), InfoType.event),
            NewsItemFragment.newInstance(getString(category_id[2]), InfoType.points),
            NewsItemFragment.newInstance(getString(category_id[3]), InfoType.news),
            NewsItemFragment.newInstance(getString(category_id[4]), InfoType.paper)
        };

        fragments = new ArrayList<Fragment>();
        fragments.addAll(Arrays.asList(subfragments));

        NewsItemAdapter adapter = new NewsItemAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragments);
        view_pager.setAdapter(adapter);
        tabs.setupWithViewPager(view_pager);

//        ImageButton edit = this.view.findViewById(R.id.button_edit);
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if ()
//            }
//        });

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