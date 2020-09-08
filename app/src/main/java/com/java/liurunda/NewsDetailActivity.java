package com.java.liurunda;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.java.liurunda.data.News;

public class NewsDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = this.getIntent();
        News news = (News) intent.getSerializableExtra("news_content");
        TextView vt = findViewById(R.id.view_title);
        vt.setText(news.title);
        TextView vm = findViewById(R.id.view_meta);
        vm.setText(DateUtil.getTextFormattedDate(news.datetime) + " " + news.source);
        TextView vc = findViewById(R.id.view_content);
        vc.setText(news.content);
    }
}
