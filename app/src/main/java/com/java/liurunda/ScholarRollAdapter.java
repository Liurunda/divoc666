package com.java.liurunda;

import android.accounts.NetworkErrorException;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.Scholar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ScholarRollAdapter extends RecyclerView.Adapter<ScholarRollAdapter.ScholarViewHolder> {
    private final ArrayList<Scholar> scholars;
//    private ExecutorService single = Executors.newSingleThreadExecutor();
//    private ExecutorService single2 = Executors.newSingleThreadExecutor();
    public static class ScholarViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout layout;
        public ScholarViewHolder(FrameLayout l) {
            super(l);
            layout = l;
        }
    }

    public ScholarRollAdapter(ArrayList<Scholar> scholars) {
        this.scholars = scholars;
    }

    @NotNull
    @Override
    public ScholarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ScholarViewHolder v = new ScholarViewHolder((FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_scholar_entry, parent, false));
        v.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scholar scholar = (Scholar) v.getTag();
                Intent intent = new Intent();
                intent.setClass(v.getContext(), ScholarDetailActivity.class);
                intent.putExtra("scholar", scholar);
                v.getContext().startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onBindViewHolder(ScholarViewHolder holder, int position) {
        final Scholar scholar = scholars.get(position);
        TextView vn = holder.layout.findViewById(R.id.viewName);
        vn.setText(scholar.nameZh.equals("") ? scholar.name : scholar.nameZh + " (" + scholar.name + ")");
        TextView vh = holder.layout.findViewById(R.id.viewH);
        vh.setText(Integer.toString(scholar.hIndex));
//        TextView va = holder.layout.findViewById(R.id.viewA);
//        va.setText(Double.toString(scholar.activity));
//        TextView vs = holder.layout.findViewById(R.id.viewS);
//        vs.setText(Double.toString(scholar.sociability));
        TextView vc = holder.layout.findViewById(R.id.viewC);
        vc.setText(Integer.toString(scholar.citations));
        TextView vp = holder.layout.findViewById(R.id.viewP);
        vp.setText(Integer.toString(scholar.publications));
        TextView vpo = holder.layout.findViewById(R.id.viewPosition);
        vpo.setText(scholar.position);
        TextView vaf = holder.layout.findViewById(R.id.viewAffiliation);
        vaf.setText(scholar.affiliation);

        ImageView img = holder.layout.findViewById(R.id.avatar);
        if (img.getTag() == null || img.getTag() != scholar.avatarUrl) {
            img.setImageBitmap(null);
        }
        img.setTag(scholar.avatarUrl);
        LinearLayout vertical = holder.layout.findViewById(R.id.layoutInfo);

        if (scholar.isPassedAway) {
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            img.setColorFilter(new ColorMatrixColorFilter(cm));
        } else {
            img.setColorFilter(null);
        }

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(img.getContext());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
        imageLoader.displayImage(scholar.avatarUrl, img);
        img.setMaxHeight(vertical.getHeight());

        holder.itemView.setTag(scholar);
    }

    @Override
    public int getItemCount() {
        return scholars.size();
    }
}
