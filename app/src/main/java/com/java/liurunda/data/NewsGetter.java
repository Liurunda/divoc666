package com.java.liurunda.data;

import android.icu.text.IDNA;
import androidx.room.Room;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class NewsGetter {
    NetClient client = new NetClient();
    static NewsGetter Getter;
    static RoomManager manager;
    private NewsGetter(){

    }
    public static void setup(RoomManager manager){
        NewsGetter.manager = manager;
    }
    public static NewsGetter Getter(){
        //CompletableFuture.runAsync(manager::initialize);
        if(Getter==null)Getter = new NewsGetter();
        return Getter;
    }
    @Deprecated
    public ArrayList<News> initial_news(){ //应当使用异步方式进行调用
        ArrayList<News> list =  new ArrayList<>();
        client.getNews(list, InfoType.news);
        return list;
    }
    public ArrayList<News> initial_news(InfoType t){ //应当使用异步方式进行调用
        ArrayList<News> list =  new ArrayList<>();
        client.getNews(list,t);
        //CompletableFuture.runAsync(()->{manager.storeNews(list);});
        return list;
    }
    public ArrayList<News> latest_news(InfoType t){ //应当使用异步方式进行调用
        ArrayList<News> list =  new ArrayList<>();
        client.getNews(list,t);
        return list;
    }
    public ArrayList<News> older_news(InfoType t){ //应当使用异步方式进行调用
        ArrayList<News> list =  new ArrayList<>();
        client.getNews(list,t);
        return list;
    }
    public ArrayList<News> search_news(String keyword){ //应当使用异步方式进行调用
        ArrayList<News> list =  new ArrayList<>();
        client.getNews(list, InfoType.news);
        return list;
    }
}