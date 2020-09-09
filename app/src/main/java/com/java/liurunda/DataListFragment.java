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
    private ArrayList<Map.Entry<String, EpidemicDataEntry>> dataSet;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM   = 1;

    public DataListAdapter(ArrayList<Map.Entry<String, EpidemicDataEntry>> data) {
        this.dataSet = data;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_epidemic_data_header, parent, false));
        } else {
            assert(viewType == TYPE_ITEM);
            return new ItemViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_epidemic_data_line, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder hold = (ItemViewHolder) holder;
            Map.Entry<String, EpidemicDataEntry> data = this.dataSet.get(position - 1);
            ((TextView) hold.itemView.findViewById(R.id.viewRegion)).setText(data.getKey());

            EpidemicDataEntry entry = data.getValue();
            ((TextView) hold.itemView.findViewById(R.id.viewConfirmed)).setText(entry.confirmed);
            ((TextView) hold.itemView.findViewById(R.id.viewCured)).setText(entry.cured);
            ((TextView) hold.itemView.findViewById(R.id.viewDead)).setText(entry.dead);
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
        public FrameLayout layout;
        public HeaderViewHolder(FrameLayout l) {
            super(l);
            layout = l;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(FrameLayout itemView) {
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
    private ArrayList<Map.Entry<String, EpidemicDataEntry>> dataList = new ArrayList<>();

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
     * @return A new instance of fragment DataListFragment.
     */
    public static DataListFragment newInstance() {
        DataListFragment fragment = new DataListFragment();
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
        this.view = inflater.inflate(R.layout.fragment_data_list, container, false);

        RecyclerView regional = this.view.findViewById(R.id.recyclerRegional);

        layoutManager = new LinearLayoutManager(regional.getContext());
        regional.setLayoutManager(layoutManager);

        adapter = new DataListAdapter(this.dataList);
        regional.setAdapter(adapter);

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

    public void setDataSet(EpidemicData newData) {
        dataList.clear();
        dataList.addAll(new ArrayList(newData.regional.entrySet()));
        getActivity().runOnUiThread(adapter::notifyDataSetChanged);
    }
}
