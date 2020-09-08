package com.java.liurunda;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.java.liurunda.data.News;

public class NewsDetailActivity extends BaseActivity {
    void shareUrl(String content, String title, String subject) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, content);
        if (title != null && !title.equals("")) {
            intent.putExtra(Intent.EXTRA_TITLE, title);
        }
        if (subject != null && !subject.equals("")) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }

        intent.putExtra(Intent.EXTRA_TITLE, title);
        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.text_share_with));
        this.startActivity(chooserIntent);
    }

    String excerpt(String content) {
        final int excerptLength = 70;
        if (content.length() > excerptLength) {
            return content.substring(0, excerptLength) + "...";
        } else {
            return content;
        }
    }

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

        ImageButton share = findViewById(R.id.button_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = news.title + "\n" + getString(R.string.text_origin) + " " + news.origin_url + "\n" + excerpt(news.content);
                shareUrl(content, news.title, news.source);
            }
        });
    }
}
