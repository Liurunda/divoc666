package com.java.liurunda;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liurunda.data.NewsGetter;
import com.java.liurunda.data.RoomManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {
    RoomManager manager;
    ExecutorService executor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        manager = new RoomManager(getApplicationContext());
        NewsGetter.setup(manager);
        executor = Executors.newCachedThreadPool();

        final FragmentManager fm = getSupportFragmentManager();

        final NewsListFragment f_newsList = NewsListFragment.newInstance();
        final DataFragment f_data = DataFragment.newInstance();
        final KnowledgeFragment f_knowledge = KnowledgeFragment.newInstance();
        final ClusterFragment f_cluster = ClusterFragment.newInstance();
        final ScholarFragment f_scholar = ScholarFragment.newInstance();

        final FragmentTransaction ft = fm.beginTransaction();
        Fragment[] f_current = {f_newsList};

        f_newsList.passCurrent(f_current);
        f_data.passCurrent(f_current);
        f_knowledge.passCurrent(f_current);
        f_cluster.passCurrent(f_current);
        f_scholar.passCurrent(f_current);

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



        fm.beginTransaction().hide(f_newsList).show(f_newsList).commit();

        BottomNavigationView nav = findViewById(R.id.bottom_nav);

        nav.setOnNavigationItemSelectedListener(item -> {
            final FragmentTransaction transaction = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.menu_news: {
                        if (f_current[0] != f_newsList) {
                            transaction.hide(f_current[0]).show(f_newsList).addToBackStack(null).commit();
                         //   f_current[0] = f_newsList;
                        }
                        break;
                    }
                    case R.id.menu_data: {
                        if (f_current[0] != f_data) {
                            transaction.hide(f_current[0]).show(f_data).addToBackStack(null).commit();
                           // f_current[0] = f_data;
                        }
                        break;
                    }
                    case R.id.menu_knowledge: {
                        if (f_current[0] != f_knowledge) {
                            transaction.hide(f_current[0]).show(f_knowledge).addToBackStack(null).commit();
                            //f_current[0] = f_knowledge;
                        }
                        break;
                    }
                    case R.id.menu_cluster: {
                        if (f_current[0] != f_cluster) {
                            transaction.hide(f_current[0]).show(f_cluster).addToBackStack(null).commit();
                            //f_current[0] = f_cluster;
                        }
                        break;
                    }
                    case R.id.menu_scholar: {
                        if (f_current[0] != f_scholar) {
                            transaction.hide(f_current[0]).show(f_scholar).addToBackStack(null).commit();
                           // f_current[0] = f_scholar;
                        }
                        break;
                    }

            }
            return true;
        });
    }
    //knowledge -> cluster -> return -> cluster
    @Override
    protected void onDestroy(){
        manager.close();
        super.onDestroy();
    }
}