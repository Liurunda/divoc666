package com.java.liurunda;

import android.os.Bundle;
import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.java.liurunda.data.Entity;
import com.java.liurunda.data.EntityGetter;

import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KnowledgeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KnowledgeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private EntityGetter Getter;
    public KnowledgeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment KnowledgeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KnowledgeFragment newInstance(/*String param1, String param2*/) {
        KnowledgeFragment fragment = new KnowledgeFragment();
        Bundle args = new Bundle();

//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Getter = EntityGetter.getInstance();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View knowledge = inflater.inflate(R.layout.fragment_knowledge, container, false);
        SearchView S = knowledge.findViewById(R.id.entity_search);
        S.setOnQueryTextListener (new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TextView text = knowledge.findViewById(R.id.someEntity).findViewById(R.id.entityName);
                CompletableFuture.supplyAsync( ()->Getter.getEntities(query) ).thenAccept((list)->{
                    text.setText(list.get(0).name);
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        }
        );
        return knowledge;
    }
}