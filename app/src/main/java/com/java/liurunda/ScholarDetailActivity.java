package com.java.liurunda;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import com.java.liurunda.data.Scholar;
import org.intellij.lang.annotations.Flow;

import java.util.concurrent.CompletableFuture;

public class ScholarDetailActivity extends AppCompatActivity {

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

        TextView vt = findViewById(R.id.viewTags);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < scholar.tags.size(); ++i) {
            builder.append(scholar.tags.get(i));
            if (i < scholar.tagsScore.size()) {
                builder.append(" (");
                builder.append(scholar.tagsScore.get(i));
                builder.append(")");
            }
            builder.append('\n');
        }
        vt.setText(builder.toString());

        if (scholar.isPassedAway) {
            GrayLinearLayout whole = findViewById(R.id.viewWhole);
            whole.setSaturation(0.0f);
        }

        ImageView img = findViewById(R.id.avatar);
        LinearLayout vertical = findViewById(R.id.layoutInfo);
        CompletableFuture.supplyAsync(scholar::getAvatarWrapper).thenAccept((bitmap) -> {
            img.setImageBitmap(bitmap);
            img.setMaxHeight(vertical.getHeight());
        });
    }
}