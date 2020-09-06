package com.java.liurunda.data;

import android.icu.text.IDNA;
import androidx.room.Room;

import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NewsGetter {
    NetClient client = new NetClient();
    Map<InfoType, News> cur_latest,cur_oldest;//TODO: 需要分不同类型的新闻分别设置...
    Map<InfoType, Integer> counter;
    static NewsGetter Getter;
    static RoomManager manager;

    private NewsGetter(){

    }
    public static void setup(RoomManager manager){
        NewsGetter.manager = manager;
    }
    public static NewsGetter Getter(){
        CompletableFuture.runAsync(manager::initialize);
        if(Getter==null)Getter = new NewsGetter();
        return Getter;
    }
//    @Deprecated
//    public ArrayList<News> initial_news(){ //应当使用异步方式进行调用
//        ArrayList<News> list =  new ArrayList<>();
//        client.getNewestNews(list, InfoType.news, 10); //初始化时，总是进行网络调用获取最新新闻，然后再和数据库中内容进行比较
//        return list;
//    }
    public ArrayList<News> initial_news(InfoType t){ //应当使用异步方式进行调用
        ArrayList<News> list =  new ArrayList<>();
        client.getNewestNews(list,t,10);
        News ender = list.get(list.size()-1);//oldest in this page
        manager.check_add_page(list);//此处必须同步进行check, 否则就不能正确显示"新闻是否已经被查看过". list中唯一可能被修改的数据域就是 haveread
        News ender_merged = list.get(list.size()-1);//merged with older record
        cur_latest.put(t, list.get(0));
        cur_oldest.put(t, list.get(list.size()-1));
        counter.put(t, list.size());
        if(ender_merged.id!="") { // no direct successor in database
            CompletableFuture.runAsync(() -> {
                ArrayList<News> listb = new ArrayList<>();
                client.getNews(listb, t, 2, 10);//此处应当先检测从ender往后连能不能连上....
                if (!listb.contains(ender)) {
                    //no overlapping
                    manager.add_link_cross_page(ender.id, listb.get(0).id);
                }
            });
        }
        return list;
    }
    public ArrayList<News> older_news(InfoType t, int size){ //应当使用异步方式进行调用
        //优先在数据库中通过 prev_id上溯。等到断开的时候再进行网络请求。

        //start from cur_oldest
        ArrayList<News> list =  new ArrayList<>();
        News oldest = cur_oldest.get(t);

        return list;
    }
    public ArrayList<News> latest_news(InfoType t){ //应当使用异步方式进行调用
        //任意时刻，展示给用户的，必然是接口返回结果中连续的一段
        //所以必须做好delta的工作...
        ArrayList<News> list =  new ArrayList<>();
        int page_size = 10;
        client.getNewestNews(list,t, page_size);
        News latest = cur_latest.get(t);
        while(!list.contains(latest)) {//翻倍20条, 40条, 80条...
            list.clear();
            page_size = page_size * 2;
            client.getNewestNews(list, t, page_size);
        }
        manager.check_add_page(list);
        //已经包含cur_latest, 进行delta操作

        cur_latest.put(t, list.get(0));
        counter.put(t, counter.get(t) + list.indexOf(latest));
        return new ArrayList<>(list.subList(0, list.indexOf(latest)-1));
    }
    public ArrayList<News> search_news(String keyword){ //应当使用异步方式进行调用
        ArrayList<News> list =  new ArrayList<>();
        client.getNews(list, InfoType.news);
        return list;
    }
    public void markNewsRead(News news){
        manager.touch(news);
    }
}