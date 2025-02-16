package com.java.liurunda;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.java.liurunda.data.Scholar;
import com.kongzue.stacklabelview.StackLabel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ScholarDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholar_detail);

        Intent intent = this.getIntent();
        Scholar scholar = (Scholar) intent.getSerializableExtra("scholar");
        TextView vn = findViewById(R.id.viewName);
        vn.setText(scholar.nameZh.equals("") ? scholar.name : scholar.nameZh + " (" + scholar.name + ")");
        TextView vp = findViewById(R.id.viewPosition);
        vp.setText(scholar.position);
        TextView va = findViewById(R.id.viewAffiliation);
        va.setText(scholar.affiliation);
        TextView v_h = findViewById(R.id.viewH);
        v_h.setText(Integer.toString(scholar.hIndex));
        TextView v_c = findViewById(R.id.viewC);
        v_c.setText(Integer.toString(scholar.citations));
        TextView v_p = findViewById(R.id.viewP);
        v_p.setText(Integer.toString(scholar.publications));
        TextView v_g = findViewById(R.id.viewG);
        v_g.setText(Integer.toString(scholar.gIndex));
        TextView v_d = findViewById(R.id.viewD);
        v_d.setText(Double.toString(scholar.diversity));
        TextView v_a = findViewById(R.id.viewA);
        v_a.setText(Double.toString(scholar.activity));
        TextView ve = findViewById(R.id.viewEdu);
        ve.setText(scholar.edu.replace("<br>", "\n"));
        TextView vb = findViewById(R.id.viewBio);
        vb.setText(scholar.bio.replace("<br>", "\n"));

        StackLabel flow = findViewById(R.id.tags);
        for (int i = 0; i < scholar.tags.size(); ++i) {
            StringBuilder builder = new StringBuilder();
            builder.append(scholar.tags.get(i));
            if (i < scholar.tagsScore.size()) {
                builder.append(" (");
                builder.append(scholar.tagsScore.get(i));
                builder.append(")");
            }
            flow.addLabel(builder.toString());
        }

        if (scholar.isPassedAway) {
            GrayLinearLayout whole = findViewById(R.id.viewWhole);
            whole.setSaturation(0.0f);
        }

        ImageView img = findViewById(R.id.avatar);
        LinearLayout vertical = findViewById(R.id.layoutInfo);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(img.getContext());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
        imageLoader.displayImage(scholar.avatarUrl, img);
        img.setMaxHeight(vertical.getHeight());
    }
}
