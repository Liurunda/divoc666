package com.java.liurunda;

import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EpidemicDataHeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EpidemicDataHeaderFragment extends Fragment {
    public EpidemicDataHeaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EpidemicDataHeaderFragment.
     */
    public static EpidemicDataHeaderFragment newInstance() {
        EpidemicDataHeaderFragment fragment = new EpidemicDataHeaderFragment();
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
        View view = inflater.inflate(R.layout.fragment_epidemic_data_header, container, false);

//        View layout = view.findViewById(R.id.layoutHeader);
//        final int totalWidth = layout.getWidth();
//        System.out.println(totalWidth + "=======================");
//        ((TextView) view.findViewById(R.id.viewHeaderRegion)).setWidth((int) (0.4 * totalWidth));
//        ((TextView) view.findViewById(R.id.viewHeaderConfirmed)).setWidth((int) (0.2 * totalWidth));
//        ((TextView) view.findViewById(R.id.viewHeaderCured)).setWidth((int) (0.2 * totalWidth));
//        ((TextView) view.findViewById(R.id.viewHeaderDead)).setWidth((int) (0.2 * totalWidth));
        return view;
    }
}
