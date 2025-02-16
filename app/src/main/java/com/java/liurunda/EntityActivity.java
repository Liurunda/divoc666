package com.java.liurunda;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.Entity;
import com.java.liurunda.data.Pair2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

class RelationAdapter extends RecyclerView.Adapter<RelationAdapter.RelationViewHolder> {
    private final ArrayList<Pair2<String, String>> forward;
    private final ArrayList<Pair2<String, String>> backward;

    static class RelationViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layout;
        public RelationViewHolder(RelativeLayout l) {
            super(l);
            layout = l;
        }
    }

    public RelationAdapter(ArrayList<Pair2<String, String>> forward, ArrayList<Pair2<String, String>> backward) {
        this.forward = forward;
        this.backward = backward;
    }

    @NotNull
    @Override
    public RelationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RelationViewHolder((RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_relations, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull RelationViewHolder holder, int position) {
        Pair2<String, String> relation;
        if (position < forward.size()) {
            relation = forward.get(position);
            ((ImageView) holder.layout.findViewById(R.id.imgForward)).setImageResource(R.drawable.baseline_keyboard_arrow_right_green_700_24dp);
        } else {
            relation = backward.get(position - forward.size());
            ((ImageView) holder.layout.findViewById(R.id.imgForward)).setImageResource(R.drawable.baseline_keyboard_arrow_left_red_700_24dp);
        }

        ((TextView) holder.layout.findViewById(R.id.viewRelationWith)).setText(relation.first);
        ((TextView) holder.layout.findViewById(R.id.viewRelationName)).setText(relation.second);
    }

    @Override
    public int getItemCount() {
        return forward.size() + backward.size();
    }
}

class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private final ArrayList<Pair2<String, String>> properties;

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public PropertyViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public PropertyAdapter(ArrayList<Pair2<String, String>> properties) {
        this.properties = properties;
    }

    @NotNull
    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PropertyViewHolder((LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_properties, parent, false));
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, int position) {
        Pair2<String, String> relation = properties.get(position);

        ((TextView) holder.layout.findViewById(R.id.viewPropertyName)).setText(relation.first);
        ((TextView) holder.layout.findViewById(R.id.viewPropertyDescription)).setText(relation.second);
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }
}

public class EntityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);

        Intent intent = this.getIntent();
        Entity entity = (Entity) intent.getSerializableExtra("entity");

        ((TextView) findViewById(R.id.viewName)).setText(entity.name);
        ((TextView) findViewById(R.id.viewHot)).setText(String.join("", Collections.nCopies(Math.min((int) Math.floor(entity.hot * 10) + 1, 10), getString(R.string.text_star))));
        ((TextView) findViewById(R.id.viewEntityDescription)).setText(entity.baidu.equals("") ? entity.enwiki : entity.baidu);

        ImageView img = findViewById(R.id.entityImage);
        img.setMaxHeight(500);
        img.setImageBitmap(null);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
        imageLoader.displayImage(entity.img_url, img);

        RecyclerView recyclerRelations = findViewById(R.id.listRelations);
        RecyclerView.LayoutManager layoutRelations = new LinearLayoutManager(this);
        recyclerRelations.setLayoutManager(layoutRelations);
        RelationAdapter adapterRelations = new RelationAdapter(entity.forwardRelations, entity.backwardRelations);
        recyclerRelations.setAdapter(adapterRelations);
        recyclerRelations.setNestedScrollingEnabled(false);

        RecyclerView recyclerProperties = findViewById(R.id.listProperties);
        RecyclerView.LayoutManager layoutProperties = new LinearLayoutManager(this);
        recyclerProperties.setLayoutManager(layoutProperties);
        PropertyAdapter adapterProperties = new PropertyAdapter(entity.properties);
        recyclerProperties.setAdapter(adapterProperties);
        recyclerProperties.setNestedScrollingEnabled(false);
    }
}
