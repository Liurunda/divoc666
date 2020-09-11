package com.java.liurunda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.java.liurunda.data.InfoType;
import com.kongzue.stacklabelview.StackLabel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
    private NewsItemAdapter adapter;
    private ViewPager view_pager;

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
        view_pager = this.view.findViewById(R.id.view_pager);
        view_pager.setOffscreenPageLimit(5);

        NewsItemFragment[] subfragments = {
            NewsItemFragment.newInstance(getString(category_id[0]), InfoType.all),
            NewsItemFragment.newInstance(getString(category_id[1]), InfoType.event),
            NewsItemFragment.newInstance(getString(category_id[2]), InfoType.points),
            NewsItemFragment.newInstance(getString(category_id[3]), InfoType.news),
            NewsItemFragment.newInstance(getString(category_id[4]), InfoType.paper)
        };

        fragments = new ArrayList<>(Arrays.asList(subfragments));

        Integer[] initials = {0, 1, 2, 3, 4};
        ArrayList<Integer> initialActivates = new ArrayList<>(Arrays.asList(initials));

        adapter = new NewsItemAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragments, initialActivates);
        view_pager.setAdapter(adapter);
        tabs.setupWithViewPager(view_pager);

        loadSettings(null);

        ImageButton edit = this.view.findViewById(R.id.button_edit);
        edit.setOnClickListener(view -> {
            View popup = View.inflate(getContext(), R.layout.fragment_edit_category, null);
            PopupWindow window = new PopupWindow(popup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            window.setOutsideTouchable(true);
            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_window_rounded, null));
            window.setElevation(16.0f);

            StackLabel displayed = popup.findViewById(R.id.displayed);
            StackLabel hidden = popup.findViewById(R.id.hidden);

            final int[] settings = {loadSettings(popup)};

            displayed.setOnLabelClickListener((index, v, s) -> {
                for (int flag: Util.CATEGORY_FLAGS) {
                    if (s.equals(getString(category_id[flag]))) {
                        if (Util.writeCategorySettings(getActivity(), settings[0] & ~(1 << flag))) {
                            settings[0] = loadSettings(popup);
                        } else {
                            Util.showSnackbar(getActivity(), getString(R.string.text_category_at_least_one));
                        }
                        break;
                    }
                }
            });

            hidden.setOnLabelClickListener((index, v, s) -> {
                for (int flag: Util.CATEGORY_FLAGS) {
                    if (s.equals(getString(category_id[flag]))) {
                        if (Util.writeCategorySettings(getActivity(), settings[0] | (1 << flag))) {
                            settings[0] = loadSettings(popup);
                        } else {
                            Util.showSnackbar(getActivity(), getString(R.string.text_category_at_least_one));
                        }
                        break;
                    }
                }
            });

            window.showAsDropDown(view);
        });

        return this.view;
    }

    int loadSettings(View view) {
        int settings = Util.loadCategorySettings(Objects.requireNonNull(getActivity()));

        StackLabel displayed = null, hidden = null;
        TextView viewMore = null;

        if (view != null) {
            displayed = view.findViewById(R.id.displayed);
            hidden = view.findViewById(R.id.hidden);

            viewMore = view.findViewById(R.id.viewMore);

            displayed.setLabels(new ArrayList<>());
            hidden.setLabels(new ArrayList<>());
        }

        ArrayList<Integer> newActivates = new ArrayList<>();
        for (int flag: Util.CATEGORY_FLAGS) {
            if ((settings & (1 << flag)) != 0) {
                if (displayed != null) {
                    displayed.addLabel(getString(category_id[flag]));
                }
                newActivates.add(flag);
            } else {
                if (hidden != null) {
                    hidden.addLabel(getString(category_id[flag]));
                }
            }
        }

        if (view != null) {
            if (settings == Util.CATEGORY_FULL) {
                hidden.setVisibility(View.GONE);
                viewMore.setVisibility(View.GONE);
            } else {
                hidden.setVisibility(View.VISIBLE);
                viewMore.setVisibility(View.VISIBLE);
            }
        }

        adapter.setNewList(newActivates);

        return settings;
    }

    public class NewsItemAdapter extends FragmentStatePagerAdapter {
        private final ArrayList<Fragment> list;
        private final ArrayList<Integer> activated;

        public NewsItemAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<Integer> activated) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.list = list;
            this.activated = activated;
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            return list.get(activated.get(position));
        }

        @Override
        public int getCount() {
            return activated.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(category_id[activated.get(position)]);
        }

        public void setNewList(ArrayList<Integer> newActivated) {
            activated.clear();
            activated.addAll(newActivated);
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NotNull Object object) {
            return POSITION_NONE;
        }
    }
}
