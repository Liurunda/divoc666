package com.java.liurunda.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NewsGetter {
    NetClient client = new NetClient();
    Map<InfoType, News> cur_latest,cur_oldest;//TODO: 需要分不同类型的新闻分别设置...
    Map<InfoType, Integer> counter;
    ArrayList<String> search_history = new ArrayList<>();
    static NewsGetter Getter;
    static RoomManager manager;

    private NewsGetter(){
        cur_latest = new HashMap<>();
        cur_oldest = new HashMap<>();
        counter = new HashMap<>();
    }
    public static void setup(RoomManager manager){
        NewsGetter.manager = manager;
    }
    public synchronized static NewsGetter Getter(){
        if(Getter==null){
            CompletableFuture.runAsync(manager::initialize);
            Getter = new NewsGetter();
        }
        return Getter;
    }
    synchronized public ArrayList<News> initial_news(InfoType t){ //应当使用异步方式进行调用
        ArrayList<News> list =  new ArrayList<>();
        int pagesize = 10;
        if(!client.getNewestNews(list,t,pagesize)) {
            manager.offline_initial(list, t, pagesize);
        }else{
            manager.updateNewest(list.get(0).id,t);
            manager.updateOldest(list.get(list.size()-1).id,t);
        }
        News ender = list.get(list.size()-1);//oldest in this page
    //    throw new NullPointerException();
        manager.check_add_page(list);//此处必须同步进行check, 否则就不能正确显示"新闻是否已经被查看过". list中唯一可能被修改的数据域就是 haveread
        News ender_merged = list.get(list.size()-1);//merged with older record
        cur_latest.put(t, list.get(0));
        cur_oldest.put(t, list.get(list.size()-1));
        counter.put(t, list.size());
        CompletableFuture.runAsync(() -> {
                manager.load_search_history(search_history);
                ArrayList<News> listb = new ArrayList<>();
                client.getNews(listb, t, 1, 100);//此处应当先检测从ender往后连能不能连上....
                manager.check_add_page(listb);
        });
        return list;
    }
    synchronized public ArrayList<News> older_news(InfoType t, int size){ //应当使用异步方式进行调用
        //assert size > 0

        //优先在数据库中通过 prev_id上溯。等到断开的时候再进行网络请求。

        //start from cur_oldest
        ArrayList<News> list =  new ArrayList<>();
        News oldest = cur_oldest.get(t);
        if(manager.link_list_prev(list, oldest.prev_id, size)){
            cur_oldest.put(t, list.get(list.size()-1));
            counter.put(t,counter.get(t)+size);
            return list;
        }else { //否则通过网络请求获取足够多的新闻
            list.clear();
            int len = counter.get(t);
            //当前给用户展示了t条新闻...假设当前最新新闻更新的条数不多
            int count_page = (len + size - 1) / size; //如果没有更多更新
            int cur_size = 0;
            boolean reached_oldest = false;
            ArrayList<News> onepage = new ArrayList<>();
            //News lastpage_end = null,thispage_start = null;
            for(int page_i=count_page;cur_size < size;++page_i){
                if(!client.getNews(onepage,t,page_i,size)){//network fail?
                    return list;
                }
                if(reached_oldest) {
                    for(int i=0;i<size && cur_size < size;++i, ++cur_size){
                        list.add(onepage.get(i));
                    }
                }
                else if(onepage.contains(oldest)){
                    reached_oldest = true;
                    for(int i=onepage.indexOf(oldest)+1;i<size && cur_size < size;++i, ++cur_size){
                        list.add(onepage.get(i));
                    }
                }
                onepage.clear();
            }
            manager.check_add_page(list);
            cur_oldest.put(t, list.get(list.size()-1));
            counter.put(t,counter.get(t)+size);
            return list;
        }
    }
    synchronized public ArrayList<News> latest_news(InfoType t){ //应当使用异步方式进行调用
        //任意时刻，展示给用户的，必然是接口返回结果中连续的一段
        //所以必须做好delta的工作...
        ArrayList<News> list =  new ArrayList<>();
        int page_size = 10;
        if(!client.getNewestNews(list,t, page_size)){
            return list;
        }
        manager.updateNewest(list.get(0).id,t);
        News latest = cur_latest.get(t);
       while(!list.contains(latest)) {//翻倍20条, 40条, 80条...
            list.clear();
            page_size = page_size * 2;
            if(!client.getNewestNews(list, t, page_size)){
                return list;
            }
        }
        manager.check_add_page(list);
        //已经包含cur_latest, 进行delta操作

        cur_latest.put(t, list.get(0));
        counter.put(t, counter.get(t) + list.indexOf(latest));
         return new ArrayList<>(list.subList(0, list.indexOf(latest)));
    }
    public ArrayList<String> search_history(){
        return search_history;
    }
    public void save_search_history(){
        manager.save_search_history(search_history);
    }
    synchronized public ArrayList<News> search_result(String keyword){ //应当使用异步方式进行调用
        //返回新闻和论文类型
        ArrayList<News> list =  new ArrayList<>();
        CompletableFuture.runAsync(()->{
            if(!search_history.contains(keyword)){
                search_history.add(keyword);
            }
            save_search_history();
        });
        manager.searchNews(list, keyword);
        return list;
    }
    public void markNewsRead(News news){
        manager.touch(news);
    }
}