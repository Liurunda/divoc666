package com.java.liurunda.data;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class NewsGetter {
    NetClient client = new NetClient();
    public ArrayList<News> initial_news(){
        ArrayList<News> list =  new ArrayList<>();
        client.getNews(list);
        return list;
    }
    public ArrayList<News> latest_news(){//不一定有?
        return new ArrayList<>();
    }
    public ArrayList<News> older_news(){ //返回10个
        return new ArrayList<>();
    }
}