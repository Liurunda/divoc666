package com.java.liurunda;

import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EpidemicDataLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EpidemicDataLineFragment extends Fragment {
    public EpidemicDataLineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EpidemicDataLineFragment.
     */
    public static EpidemicDataLineFragment newInstance() {
        EpidemicDataLineFragment fragment = new EpidemicDataLineFragment();
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
        return inflater.inflate(R.layout.fragment_epidemic_data_line, container, false);
    }
}