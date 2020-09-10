package com.java.liurunda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liurunda.data.Entity;
import com.java.liurunda.data.EntityGetter;
import com.java.liurunda.data.News;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.EntityViewHolder> {
    private ArrayList<Entity> entities;

    public static class EntityViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public EntityViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public EntityAdapter(Context context, ArrayList<Entity> entities) {
        this.entities = entities;
    }

    @NotNull
    @Override
    public EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntityViewHolder((LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_entity_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(EntityViewHolder holder, int position) {
        Entity entity = entities.get(position);

        ((TextView) holder.layout.findViewById(R.id.viewEntityName)).setText(entity.name);
        ((TextView) holder.layout.findViewById(R.id.viewEntityHot)).setText(String.join("", Collections.nCopies(Math.min((int) Math.floor(entity.hot * 10) + 1, 10), holder.layout.getContext().getString(R.string.text_star))));

        holder.itemView.setOnClickListener(view -> {
            Entity entity1 = (Entity) view.getTag();
            Intent intent = new Intent();
            intent.setClass(view.getContext(), EntityActivity.class);
            intent.putExtra("entity", entity1);
            view.getContext().startActivity(intent);
        });
        holder.itemView.setTag(entity);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }
}

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KnowledgeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KnowledgeFragment extends Fragment {
    private EntityGetter Getter;
    private ArrayList<Entity> list = new ArrayList<>();

    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;
    private EntityAdapter adapter;

    public KnowledgeFragment() {
        // Required empty public constructor
    }

    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v: viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment KnowledgeFragment.
     */
    public static KnowledgeFragment newInstance() {
        KnowledgeFragment fragment = new KnowledgeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Getter = EntityGetter.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View knowledge = inflater.inflate(R.layout.fragment_knowledge, container, false);

        SearchView S = knowledge.findViewById(R.id.entity_search);
        ArrayList<View> searchList = new ArrayList<>();
        searchList.add(S);

        recycler = knowledge.findViewById(R.id.entityList);

        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        adapter = new EntityAdapter(knowledge.getContext(), list);
        recycler.setAdapter(adapter);

        S.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideSoftKeyboard(knowledge.getContext(), searchList);
                CompletableFuture.supplyAsync(() -> Getter.getEntities(query)).thenAccept((entityList) -> {
                    Util.showSnackbar(getActivity(), getResources().getQuantityString(R.plurals.text_knowledge_entries_found, entityList.size(), entityList.size()));
                    list.clear();
                    list.addAll(entityList);
                    getActivity().runOnUiThread(adapter::notifyDataSetChanged);
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

        return knowledge;
    }
}