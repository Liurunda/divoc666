package com.java.liurunda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liurunda.data.EventGroup;
import com.java.liurunda.data.NewsEvent;
import com.kongzue.stacklabelview.StackLabel;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public ArrayList<NewsEvent> list;
    EventAdapter(ArrayList<NewsEvent> list){
        this.list = list;
    }
    class EventHolder extends RecyclerView.ViewHolder{
        public EventHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new EventAdapter.EventHolder(
                (FrameLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cluster_one_event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        View view = holder.itemView;
        ((TextView)(view.findViewById(R.id.eventContent))).setText(list.get(position).title);
        ((TextView)(view.findViewById(R.id.eventTime))).setText(DateUtil.getTextFormattedDate(list.get(position).datetime));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<EventGroup> list;
    GroupAdapter(ArrayList<EventGroup> list){
        this.list = list;
    }
    class OneGroupHolder extends RecyclerView.ViewHolder{
        public OneGroupHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
        public void renderGroup(EventGroup eventGroup){
            String[] keywords = eventGroup.keyword.split(",");
            StackLabel tags = itemView.findViewById(R.id.groups);
            tags.setLabels(new ArrayList<>());
            for (String keyword: keywords) {
                tags.addLabel(keyword);
            }
            RecyclerView someGroup = this.itemView.findViewById(R.id.groupEvents);
            someGroup.setLayoutManager(new LinearLayoutManager(this.itemView.getContext(),LinearLayoutManager.HORIZONTAL,false) {
                @Override
                public boolean isAutoMeasureEnabled() {
                    return true;
                }
            });
            someGroup.setAdapter(new EventAdapter(eventGroup.list));
            someGroup.setNestedScrollingEnabled(false);
        }
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new GroupAdapter.OneGroupHolder(
                (FrameLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cluster_one_group, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((OneGroupHolder)holder).renderGroup(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClusterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClusterFragment extends Fragment {
    private RecyclerView groups;
    private GroupAdapter groupAdapter;
    ArrayList<EventGroup> list = new ArrayList<>();
    public ClusterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClusterFragment.
     */
    public static ClusterFragment newInstance() {
        ClusterFragment fragment = new ClusterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputStream cluster = getResources().openRawResource(R.raw.cluster);
        Scanner scan = new Scanner(cluster);
        while(scan.nextLine().equals("newgroup")){
            list.add(EventGroup.read(scan));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cluster, container, false);

        groups = view.findViewById(R.id.clusterGroups);
        groupAdapter = new GroupAdapter(list);
        groups.setAdapter(groupAdapter);
        groups.setLayoutManager(new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean isAutoMeasureEnabled() {
                return true;
            }
        });

        BottomNavigationView nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav);
        final Boolean[] isBottomShow = {true};

        groups.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY - oldScrollY > 0 && isBottomShow[0]) {
                isBottomShow[0] = false;
                nav.animate().translationY(nav.getHeight());
            } else if (scrollY - oldScrollY < 0 && !isBottomShow[0]) {
                isBottomShow[0] = true;
                nav.animate().translationY(0);
            }
        });

        return view;
    }
    Fragment[] Current;
    public void passCurrent(Fragment[] f_current) {
        Current = f_current;
    }

    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            Current[0]=this;
        }
    }

}
