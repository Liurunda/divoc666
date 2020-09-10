package com.java.liurunda;

import android.os.Bundle;
import android.text.GetChars;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import com.java.liurunda.data.Entity;
import com.java.liurunda.data.EntityGetter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
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
    private static String avatarUrl = "avatar";
    private EntityGetter Getter;
    private int counter;
    private ArrayList<Entity> list = new ArrayList<>();
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
    static void render(View entity, Entity E){
        ((TextView)entity.findViewById(R.id.entityName)).setText(E.name);
        ((TextView)entity.findViewById(R.id.entityHot)).setText(E.img_url);
        //((TextView)entity.findViewById(R.id.entityHot)).setText("Hot:" + Double.toString(E.hot));
        ImageView img = entity.findViewById(R.id.entityImage);
        img.setMaxHeight(500);
        img.setImageBitmap(null);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(img.getContext());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
        imageLoader.displayImage(E.img_url, img);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View knowledge = inflater.inflate(R.layout.fragment_knowledge, container, false);
        SearchView S = knowledge.findViewById(R.id.entity_search);
        View entity = knowledge.findViewById(R.id.someEntity);
        Toast t = Toast.makeText(entity.getContext(), "No more Entities", Toast.LENGTH_SHORT);
        S.setOnQueryTextListener (new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CompletableFuture.supplyAsync( ()->Getter.getEntities(query) ).thenAccept((entitylist)->{
                    list = entitylist;
                    counter = 0;
                    render(entity, list.get(0));
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        }
        );
        Button prev = knowledge.findViewById(R.id.prevEntity);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter!=0){
                    counter --;
                    render(entity, list.get(counter));
                }else{
                    t.show();
                }
            }
        });
        Button next = knowledge.findViewById(R.id.nextEntity);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (counter + 1 < list.size()) {
                        counter++;
                        render(entity, list.get(counter));
                    }else{
                       t.show();
                    }
            }
        });
        return knowledge;
    }
}