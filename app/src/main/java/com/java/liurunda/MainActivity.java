package com.java.liurunda;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.java.liurunda.data.News;
import com.java.liurunda.data.NewsGetter;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final FragmentManager fm = getSupportFragmentManager();
        final NewsListFragment f_newsList = NewsListFragment.newInstance();
        final DataFragment f_data = DataFragment.newInstance();
        final KnowledgeFragment f_knowledge = KnowledgeFragment.newInstance();
        final ClusterFragment f_cluster = ClusterFragment.newInstance();
        final ScholarFragment f_scholar = ScholarFragment.newInstance();

        final FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, f_newsList);
        ft.add(R.id.fragment, f_data);
        ft.add(R.id.fragment, f_knowledge);
        ft.add(R.id.fragment, f_cluster);
        ft.add(R.id.fragment, f_scholar);
        ft.hide(f_data);
        ft.hide(f_knowledge);
        ft.hide(f_cluster);
        ft.hide(f_scholar);
        ft.commit();

        final Fragment[] f_current = {f_newsList};

        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnNavigationItemSelectedListener(item -> {
            final FragmentTransaction transaction = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.menu_news: {
                    transaction.hide(f_current[0]).show(f_newsList).commit();
                    f_current[0] = f_newsList;
                    break;
                }
                case R.id.menu_data: {
                    transaction.hide(f_current[0]).show(f_data).commit();
                    f_current[0] = f_data;
                    break;
                }
                case R.id.menu_knowledge: {
                    transaction.hide(f_current[0]).show(f_knowledge).commit();
                    f_current[0] = f_knowledge;
                    break;
                }
                case R.id.menu_cluster: {
                    transaction.hide(f_current[0]).show(f_cluster).commit();
                    f_current[0] = f_cluster;
                    break;
                }
                case R.id.menu_scholar: {
                    transaction.hide(f_current[0]).show(f_scholar).commit();
                    f_current[0] = f_scholar;
                    break;
                }
            }
            return true;
        });
    }
}