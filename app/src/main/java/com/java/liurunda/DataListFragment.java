package com.java.liurunda;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liurunda.data.EpidemicData;
import com.java.liurunda.data.EpidemicDataEntry;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class DataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Map.Entry<String, EpidemicData>> dataSet;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM   = 1;

    public DataListAdapter(ArrayList<Map.Entry<String, EpidemicData>> data) {
        this.dataSet = data;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder((TableRow) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_epidemic_data_header, parent, false));
        } else {
            assert(viewType == TYPE_ITEM);
            return new ItemViewHolder((TableRow) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_epidemic_data_line, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder hold = (ItemViewHolder) holder;
            Map.Entry<String, EpidemicData> data = this.dataSet.get(position - 1);
            ((TextView) hold.itemView.findViewById(R.id.viewRegion)).setText(data.getKey());

            ArrayList<EpidemicDataEntry> entries = data.getValue().entries;
            EpidemicDataEntry latest = entries.get(entries.size() - 1);
            ((TextView) hold.itemView.findViewById(R.id.viewConfirmed)).setText(latest.confirmed);
            ((TextView) hold.itemView.findViewById(R.id.viewCured)).setText(latest.cured);
            ((TextView) hold.itemView.findViewById(R.id.viewDead)).setText(latest.dead);
        } else {
            assert(holder instanceof HeaderViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TableRow layout;
        public HeaderViewHolder(TableRow l) {
            super(l);
            layout = l;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(TableRow itemView) {
            super(itemView);
        }
    }
}


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

    private ArrayList<Map.Entry<String, EpidemicData>> dataList = new ArrayList<>();

    private View view;
    private RecyclerView.LayoutManager layoutManager;
    private DataListAdapter adapter;

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

        RecyclerView regional = this.view.findViewById(R.id.recyclerRegional);

        layoutManager = new LinearLayoutManager(getContext());
        regional.setLayoutManager(layoutManager);

        adapter = new DataListAdapter(this.dataList);
        regional.setAdapter(adapter);

        return this.view;
    }

    public void setDataSet(HashMap<String, EpidemicData> newSet) {
        dataList.clear();
        dataList.addAll(new ArrayList(newSet.entrySet()));
        getActivity().runOnUiThread(adapter::notifyDataSetChanged);
    }
}
