package com.java.liurunda.data;

import android.app.Application;
import android.content.Context;
import android.icu.text.IDNA;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;

public class RoomManager{
    private final String DATABASE_NAME = "news";
    NewsBase nbase;
    public RoomManager(Context application){
        nbase = Room.databaseBuilder(application,NewsBase.class,DATABASE_NAME)
                .createFromAsset("news.db")
                .build();
    }
    public void initialize(){
        //MetaNews[] m =  nbase.metaDao().queryMeta(3);
        //System.out.println("m.length="+m.length);
        //MetaNews ma = new MetaNews(InfoType.news,0,0);
        //nbase.metaDao().insertMeta(ma);
        boolean clear = true;
        if(clear) {
       //     nbase.metaDao().clearMeta();
       //     nbase.newsDao().clearNews();
        }
        //MetaNews[] n = nbase.metaDao().queryMeta(InfoType.news.ordinal());

    }
    public void storeNews(ArrayList<News> list){
        //if news not in database: store
        //if news in database: read from database and modify list
        for(News news: list){
            //nbase.newsDao().loadNewsBetweenIdsWithType(news.new_id,news.new_id,news.infoType);
        }
    }
}
