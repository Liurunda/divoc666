package com.java.liurunda;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liurunda.data.EpidemicData;
import com.java.liurunda.data.EpidemicDataEntry;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.ItemViewHolder> {
    public ArrayList<Pair<String, EpidemicDataEntry>> dataSet;

    public DataListAdapter(ArrayList<Pair<String, EpidemicDataEntry>> data) {
        this.dataSet = data;
    }

    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_epidemic_data_line, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemViewHolder hold = (ItemViewHolder) holder;
        Pair<String, EpidemicDataEntry> data = this.dataSet.get(position);
        ((TextView) hold.itemView.findViewById(R.id.viewRegion)).setText(data.first);
        EpidemicDataEntry entry = data.second;
        ((TextView) hold.itemView.findViewById(R.id.viewConfirmed)).setText(Integer.toString(entry.confirmed));
        ((TextView) hold.itemView.findViewById(R.id.viewCured)).setText(Integer.toString(entry.cured));
        ((TextView) hold.itemView.findViewById(R.id.viewDead)).setText(Integer.toString(entry.dead));

        if (position % 2 == 1) {
            hold.itemView.findViewById(R.id.layoutBackground).setBackgroundResource(R.color.bkgColorEpidemicLine);
        } else {
            hold.itemView.findViewById(R.id.layoutBackground).setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
       return dataSet.size();
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
    private ArrayList<Pair<String, EpidemicDataEntry>> dataList;
    private ArrayList<EpidemicDataEntry> days;

    private Fragment parent;
    private View view;
    private RecyclerView.LayoutManager layoutManager;
    private DataListAdapter adapter;

    private enum ColumnId {
        Region,
        Confirmed,
        Cured,
        Dead,
    };

    private ColumnId lastSortIndex = ColumnId.Confirmed;
    private boolean lastSortAscending = false;

    public DataListFragment() {
        // Required empty public constructor
        dataList = new ArrayList<>();
        days = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DataListFragment.
     */
    public static DataListFragment newInstance(Fragment parent) {
        DataListFragment fragment = new DataListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.parent = parent;
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

        layoutManager = new LinearLayoutManager(this.view.getContext());
        regional.setLayoutManager(layoutManager);
        adapter = new DataListAdapter(this.dataList);
        regional.setAdapter(adapter);
        getActivity().runOnUiThread(()->adapter.notifyDataSetChanged());

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
        updated.setText(getString(R.string.text_last_update) + DateUtil.getTextFormattedDate(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME)));

        SwipeRefreshLayout refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);

        refresh.setOnRefreshListener(() -> {
            ((DataFragment) parent).notifyRefresh();
            refresh.setRefreshing(false);
        });

        LineChart chart = view.findViewById(R.id.chartOverall);
        chart.setNoDataText(getString(R.string.text_no_data));

        LinearLayout region = view.findViewById(R.id.titleRegion);
        region.setOnClickListener((ignored) -> {
            if (lastSortIndex == ColumnId.Region) {
                lastSortAscending = !lastSortAscending;
            } else {
                lastSortIndex = ColumnId.Region;
                lastSortAscending = true;
            }
            sort(lastSortIndex, lastSortAscending);
            if (adapter != null) {
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });

        LinearLayout confirmed = view.findViewById(R.id.titleConfirmed);
        confirmed.setOnClickListener((ignored) -> {
            if (lastSortIndex == ColumnId.Confirmed) {
                lastSortAscending = !lastSortAscending;
            } else {
                lastSortIndex = ColumnId.Confirmed;
                lastSortAscending = false;
            }
            sort(lastSortIndex, lastSortAscending);
            if (adapter != null) {
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });

        LinearLayout cured = view.findViewById(R.id.titleCured);
        cured.setOnClickListener((ignored) -> {
            if (lastSortIndex == ColumnId.Cured) {
                lastSortAscending = !lastSortAscending;
            } else {
                lastSortIndex = ColumnId.Cured;
                lastSortAscending = false;
            }
            sort(lastSortIndex, lastSortAscending);
            if (adapter != null) {
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });

        LinearLayout dead = view.findViewById(R.id.titleDead);
        dead.setOnClickListener((ignored) -> {
            if (lastSortIndex == ColumnId.Dead) {
                lastSortAscending = !lastSortAscending;
            } else {
                lastSortIndex = ColumnId.Dead;
                lastSortAscending = false;
            }
            sort(lastSortIndex, lastSortAscending);
            if (adapter != null) {
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });

        return this.view;
    }

    void setDataSetStyle(LineDataSet set, int color) {
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawFilled(true);
        set.setFillColor(color);
        set.setColor(color);
        set.setCubicIntensity(0.2f);
        set.setDrawCircleHole(false);
        set.setCircleColor(color);
    }

    class Formatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            if (Math.round(value) == value) {
                return LocalDate.now().minusDays(days.size() - 1).plusDays((long) value).toString();
            } else {
                return "";
            }
        }
    }

    public void setDataSet(EpidemicData newData) {
        // Update regional data table
        dataList.clear();

        newData.regional.forEach((name, data)->{
            dataList.add(Pair.create(name, data));
        });

        sort(lastSortIndex, lastSortAscending);

        if (adapter != null) {
            getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }

        days.clear();
        days.addAll(newData.overall);

        // Update overall data chart
        LineChart chart = view.findViewById(R.id.chartOverall);
        ArrayList<Entry> confirmed = new ArrayList<>(), cured = new ArrayList<>(), dead = new ArrayList<>();

        for (int i = 0; i < days.size(); ++i) {
            confirmed.add(new Entry(i, days.get(i).confirmed));
            cured.add(new Entry(i, days.get(i).cured));
            dead.add(new Entry(i, days.get(i).dead));
        }

        LineDataSet dataConfirmed = new LineDataSet(confirmed, getString(R.string.text_data_confirmed));
        setDataSetStyle(dataConfirmed, Color.BLUE);

        LineDataSet dataCured = new LineDataSet(cured, getString(R.string.text_data_cured));
        setDataSetStyle(dataCured, Color.GREEN);

        LineDataSet dataDead = new LineDataSet(dead, getString(R.string.text_data_dead));
        setDataSetStyle(dataDead, Color.RED);

        LineData lines = new LineData(dataConfirmed);
        lines.addDataSet(dataCured);
        lines.addDataSet(dataDead);

        chart.setData(lines);

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setValueFormatter(new Formatter());
        chart.getXAxis().setLabelRotationAngle(45.0f);

        chart.invalidate();

        // Update time
        TextView updated = view.findViewById(R.id.viewLastUpdate);
        updated.setText(getString(R.string.text_last_update) + DateUtil.getTextFormattedDate(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME)));
    }

    public void sort(ColumnId column, boolean ascending) {
        Drawable image = getResources().getDrawable(ascending ? R.drawable.baseline_keyboard_arrow_up_black_24dp : R.drawable.baseline_keyboard_arrow_down_black_24dp, null);

        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            ((ImageView) view.findViewById(R.id.sortRegion)).setImageDrawable(column == ColumnId.Region ? image : null);
            ((ImageView) view.findViewById(R.id.sortConfirmed)).setImageDrawable(column == ColumnId.Confirmed ? image : null);
            ((ImageView) view.findViewById(R.id.sortCured)).setImageDrawable(column == ColumnId.Cured ? image : null);
            ((ImageView) view.findViewById(R.id.sortDead)).setImageDrawable(column == ColumnId.Dead ? image : null);
        });

        dataList.sort((t1, t2) -> {
            if (column == ColumnId.Region) {
                return t1.first.compareTo(t2.first) * (ascending ? 1 : -1);
            } else if (column == ColumnId.Confirmed) {
                return Integer.compare(t1.second.confirmed, t2.second.confirmed) * (ascending ? 1 : -1);
            } else if (column == ColumnId.Cured) {
                return Integer.compare(t1.second.cured, t2.second.cured) * (ascending ? 1 : -1);
            } else {
                return Integer.compare(t1.second.dead, t2.second.dead) * (ascending ? 1 : -1);
            }
        });
    }
}
