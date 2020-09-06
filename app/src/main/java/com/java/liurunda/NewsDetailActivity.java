package com.java.liurunda;

import android.content.Intent;
import android.widget.TextView;
import android.os.Bundle;
import com.java.liurunda.data.News;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class NewsDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        News news = (News) intent.getSerializableExtra("news_content");
        TextView vt = findViewById(R.id.view_title);
        vt.setText(news.title);
        TextView vm = findViewById(R.id.view_meta);
        vm.setText(DateUtil.getTextFormattedDate(ZonedDateTime.parse(news.datetime, DateTimeFormatter.RFC_1123_DATE_TIME)) + " " + news.source);
        TextView vc = findViewById(R.id.view_content);
        vc.setText(news.content);
    }
}
