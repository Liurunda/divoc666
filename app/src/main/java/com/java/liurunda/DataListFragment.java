package com.java.liurunda;

import android.os.Bundle;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataListFragment extends Fragment {
    private static final String ARG_PARAM1 = "category";
    private static final String ARG_PARAM2 = "domestic";
    private String mParam1;
    private boolean mParam2;

    private View view;

    public DataListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Tab title.
     * @param param2 Domestic data or not.
     * @return A new instance of fragment DataListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataListFragment newInstance(String param1, boolean param2) {
        DataListFragment fragment = new DataListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2 ? 1 : 0);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = (getArguments().getInt(ARG_PARAM2) != 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_data_list, container, false);

        BottomNavigationView nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav);
        NestedScrollView nested = view.findViewById(R.id.nested);

        final Boolean[] isBottomShow = {true};

        nested.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY - oldScrollY > 0 && isBottomShow[0]) {
                isBottomShow[0] = false;
                nav.animate().translationY(nav.getHeight());
            } else if (scrollY - oldScrollY < 0 && !isBottomShow[0]) {
                isBottomShow[0] = true;
                nav.animate().translationY(0);
            }
        });

        TextView updated = this.view.findViewById(R.id.viewLastUpdate);
        updated.setText(DateUtil.getTextFormattedDate(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME)));

        return this.view;
    }
}
