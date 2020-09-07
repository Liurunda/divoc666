package com.java.liurunda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.java.liurunda.data.Scholar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ScholarRollAdapter extends RecyclerView.Adapter<ScholarRollAdapter.ScholarViewHolder> {
    private final ArrayList<Scholar> scholars;

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
//        v.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                News news = (News) v.getTag();
////                if (news.infoType == InfoType.news.ordinal() || news.infoType == InfoType.paper.ordinal()) {
////                    Intent intent = new Intent();
////                    intent.setClass(v.getContext(), NewsDetailActivity.class);
////                    intent.putExtra("news_content", news);
////                    v.getContext().startActivity(intent);
////                }
//            }
//        });
        return v;
    }

    @Override
    public void onBindViewHolder(ScholarViewHolder holder, int position) {
        final Scholar scholar = scholars.get(position);
        TextView vn = holder.layout.findViewById(R.id.viewName);
        vn.setText(scholar.name);
        TextView vh = holder.layout.findViewById(R.id.viewH);
        vh.setText(Integer.toString(scholar.hIndex));
        TextView va = holder.layout.findViewById(R.id.viewA);
        va.setText(Double.toString(scholar.activity));
        TextView vs = holder.layout.findViewById(R.id.viewS);
        vs.setText(Double.toString(scholar.sociability));
        TextView vc = holder.layout.findViewById(R.id.viewC);
        vc.setText(Integer.toString(scholar.citations));
        TextView vp = holder.layout.findViewById(R.id.viewP);
        vp.setText(Integer.toString(scholar.publications));
        holder.itemView.setTag(scholar);
    }

    @Override
    public int getItemCount() {
        return scholars.size();
    }
}
