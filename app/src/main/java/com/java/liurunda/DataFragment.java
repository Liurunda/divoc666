package com.java.liurunda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataFragment extends Fragment {
    private View view;
    private ArrayList<Fragment> fragments;
    private final int[] category_id = {R.string.tab_domestic, R.string.tab_global};

    public DataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DataFragment.
     */
    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
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
        this.view = inflater.inflate(R.layout.fragment_data, container, false);

        TabLayout tabs = this.view.findViewById(R.id.tabs);
        ViewPager view_pager = this.view.findViewById(R.id.view_pager);

        DataListFragment[] subfragments = {
                DataListFragment.newInstance(getString(category_id[0]), true),
                DataListFragment.newInstance(getString(category_id[1]), false)
        };

        fragments = new ArrayList<Fragment>();
        fragments.addAll(Arrays.asList(subfragments));

        DataListAdapter adapter = new DataListAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragments);
        view_pager.setAdapter(adapter);
        tabs.setupWithViewPager(view_pager);
        return this.view;
    }

    public class DataListAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public DataListAdapter(FragmentManager fm, List<Fragment> list) {
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
