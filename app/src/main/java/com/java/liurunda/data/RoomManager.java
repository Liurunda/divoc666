package com.java.liurunda.data;

import android.content.Context;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class RoomManager{
    private final String DATABASE_NAME = "news";
    private HashMap<InfoType,String> tmp_oldest_id = new HashMap<>();
    NewsBase nbase;
    public RoomManager(Context application){
        nbase = Room.databaseBuilder(application,NewsBase.class,DATABASE_NAME)
                .build();
    }
    public void initialize(){
        boolean clear = false;
        if(clear) {
            nbase.metaDao().clearMeta();
            //nbase.newsDao().clearNews();
        }
        //MetaNews[] n = nbase.metaDao().queryMeta(InfoType.news.ordinal());

    }
    public void add_link_cross_page(String newer_id, String older_id){ // newer_id.prev = older_id
        News[] older = nbase.newsDao().loadNewsId(older_id);
        News[] newer = nbase.newsDao().loadNewsId(newer_id);
        if(older.length == 1 && newer.length == 1){
            if(!newer[0].prev_id .equals(older_id) ){
                newer[0].prev_id = older_id;
                nbase.newsDao().updateNews(newer[0]);
            }
            if(!older[0].next_id.equals(newer_id)){
                older[0].next_id = newer_id;
                nbase.newsDao().updateNews(older[0]);
            }
        }
    }

    public void updateNewest(String latest_id, InfoType t){

        nbase.newsDao().insertNews(new News(Integer.toString(t.ordinal()),t,latest_id));
    }


    public void check_add_page(ArrayList<News> list){//only add link inside this page
        //if news not in database: store
        //if news in database: read from database and modify list
        try {
                int L = list.size();
                for (int i = 1; i < L; ++i) {
                    list.get(i).next_id = list.get(i - 1).id;//next_id: newer
                    list.get(i - 1).prev_id = list.get(i).id;//prev_id: older
                }
                boolean[] differ = new boolean[1];
                for (int i = 0; i < L; ++i) {
                    News[] old = nbase.newsDao().loadNewsId(list.get(i).id);
                    if (old.length != 0) {
                        list.set(i, list.get(i).merge_with_older(old[0], differ));
                        if (differ[0]) {
                            nbase.newsDao().updateNews(list.get(i));
                        }
                    } else {
                        nbase.newsDao().insertNews(list.get(i));
                    }
                }

        }catch(NullPointerException e){
            //do nothing
        }
        //一个事实: 任何基于"前一页最后一个" 和 "下一页第一个" 之间建立的连接都是不可靠的
        //但不妨认为，如果连续两次同页面大小连续页码的访问获取的内容中没有重叠, 那么前页最后一个和下页第一个之间具备可靠的先后关系(假设更新速度不至于秒出若干条)
        //2*3*5*7*11*13*17 = 510510
        //只有同一页返回的结果之间才能建立靠谱的连接
        //where should we change?
        //next_id, prev_id...
        //merge
    }
    public void touch(News news){//mark it as read
        CompletableFuture.runAsync(()-> {
                    News origin[] = nbase.newsDao().loadNewsId(news.id);
                    if (origin.length > 0 && origin[0].haveread == 0) {
                        origin[0].haveread = 1;
                        nbase.newsDao().updateNews(origin[0]);
                    }
                }
        );
    }
    public boolean link_list_prev(ArrayList<News> list, String oldest_prev_id, int size){
        String cur_id = oldest_prev_id;
        for(int i=0;i<size;++i){
            if(cur_id.equals(""))return false;
            News[] prev_news = nbase.newsDao().loadNewsId(cur_id);
            if(prev_news.length==0)return false;
            list.add(prev_news[0]);
            cur_id = prev_news[0].prev_id;
        }
        return true;
    }
    public void updateOldest(String id, InfoType t){
        tmp_oldest_id.put(t,id);
    }
    public void offline_initial(ArrayList<News> list, InfoType t, int size){
        News[] meta = nbase.newsDao().loadNewsId(Integer.toString(t.ordinal()));
        if(meta.length!=0){
            String offline_latest_id = meta[0].title;
            link_list_prev(list, offline_latest_id,size);
            tmp_oldest_id.put(t, list.get(list.size() - 1).prev_id);
        }else{
            return;
        }
    }
    public void searchNews(ArrayList<News> list, String keyword){
        News a[] = nbase.newsDao().searchNewsTitleLikeKeywords("%"+keyword+"%");
        News b[] = nbase.newsDao().searchNewsContentLikeKeywords("%"+keyword+"%");
        Arrays.stream(a).forEach(e->list.add(e));
        Arrays.stream(b).filter(e->!list.contains(e)).forEach(e->list.add(e));
    }
    public void load_search_history(ArrayList<String> history){
        MetaNews[] meta = nbase.metaDao().queryMeta(InfoType.all);
        if(meta.length!=0){
            history.clear();
            Arrays.stream(meta[0].history.split(",")).forEach(s->history.add(s));
        }else {
            history.clear();
        }
    }
    public void save_search_history(ArrayList<String> history){
        StringBuilder builder = new StringBuilder();
        for(String s:history){
            builder.append(s);
            builder.append(",");
        }
        if(builder.length()>0) builder.deleteCharAt(builder.length()-1);
        nbase.metaDao().insertMeta(new MetaNews(InfoType.all, builder.toString()));
    }
    public void close(){
        nbase.close();
    }
}
