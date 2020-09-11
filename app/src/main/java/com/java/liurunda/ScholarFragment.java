package com.java.liurunda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liurunda.data.Scholar;
import com.java.liurunda.data.ScholarGetter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScholarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScholarFragment extends Fragment {
    private View view;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;
    private ScholarRollAdapter adapter;
    private final ScholarGetter getter = ScholarGetter.getInstance();

    private ArrayList<Scholar> scholars = new ArrayList<>();

    public ScholarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScholarFragment.
     */
    public static ScholarFragment newInstance() {
        ScholarFragment fragment = new ScholarFragment();
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
        this.view = inflater.inflate(R.layout.fragment_scholar, container, false);

        recycler = this.view.findViewById(R.id.scholarRoll);

        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        adapter = new ScholarRollAdapter(this.scholars);
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

        CompletableFuture.supplyAsync(getter::getScholars).thenAccept((list) -> {
            this.scholars.addAll(list);
            getActivity().runOnUiThread(adapter::notifyDataSetChanged);
        });

        return this.view;
    }
    Fragment[] Current;
    public void passCurrent(Fragment[] f_current) {
        Current = f_current;
    }

    static boolean flag = false;
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            Current[0]=this;
        }
    }

}