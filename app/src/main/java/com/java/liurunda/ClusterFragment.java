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
import com.java.liurunda.data.EventGroup;
import com.java.liurunda.data.NewsEvent;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClusterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        ((TextView)(view.findViewById(R.id.eventTime))).setText(list.get(position).datetime);
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
            ((TextView)this.itemView.findViewById(R.id.groupName)).setText(eventGroup.keyword);
            RecyclerView someGroup = this.itemView.findViewById(R.id.groupEvents);
            someGroup.setLayoutManager(new LinearLayoutManager(this.itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));
            someGroup.setAdapter(new EventAdapter(eventGroup.list));
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
        //((Textview)holder.itemView.findViewById(R.id.groupName)).setText("This is a group");

        ((OneGroupHolder)holder).renderGroup(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
public class ClusterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private String result;
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
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment ClusterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClusterFragment newInstance(/*String param1, String param2*/) {
        ClusterFragment fragment = new ClusterFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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
        ((TextView)view.findViewById(R.id.clusterTop).findViewById(R.id.CLtitle)).setText(this.result);

        groups = view.findViewById(R.id.clusterTop).findViewById(R.id.clusterGroups);
        groupAdapter = new GroupAdapter(list);
        groups.setAdapter(groupAdapter);
        groups.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
}