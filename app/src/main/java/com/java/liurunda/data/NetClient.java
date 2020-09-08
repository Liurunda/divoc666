package com.java.liurunda.data;

import android.icu.text.IDNA;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.json.*;

class getNewsList implements Callback{
    ArrayList<News> list;
    public getNewsList(ArrayList<News> list){
        this.list = list;
    }
    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        System.out.println("getNewsList failed");
    }

    @Override
    public void onResponse(@NotNull Call call, Response response) throws IOException {
        String resp = response.body().string();
        try {
            JSONObject jj =  new JSONObject(resp);
            JSONArray data = jj.getJSONArray("data");
            for(int i=0;i<data.length();++i){
                list.add(new News(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
public class NetClient {
    OkHttpClient client = new OkHttpClient();
    final String NEWS_LIST = "https://covid-dashboard.aminer.cn/api/events/list";
    String news_list(InfoType infoType,int page, int pagesize){
        return NEWS_LIST + "?type=" + infoType.name()
                + "&page=" + page
                + "&size=" + pagesize;
    }
    public NetClient(){

    }
    void getNews(ArrayList<News> list, InfoType t, int page, int size){
        Request request = new Request.Builder()
                .url(news_list(t,page,size))
                .build();
        try {
            final Response response = client.newCall(request).execute();
            String resp = response.body().string();
            try {
                JSONObject jj =  new JSONObject(resp);
                JSONArray data = jj.getJSONArray("data");
                for(int i=0;i<data.length();++i){
                    list.add(new News(data.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void getNewestNews(ArrayList<News> list, InfoType t, int size){ // 获取最新新闻时，总是进行网络请求(因为不知道当前数据库中是否最新)
        Request request = new Request.Builder()
                .url(news_list(t,1,size))
                .build();
        try {
            final Response response = client.newCall(request).execute();
            String resp = response.body().string();
            try {
                JSONObject jj =  new JSONObject(resp);
                JSONArray data = jj.getJSONArray("data");
                for(int i=0;i<data.length();++i){
                    list.add(new News(data.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void getNews(ArrayList<News> list, InfoType t){
        Request request = new Request.Builder()
                .url(news_list(t,1,15))
                .build();
        try {
            final Response response = client.newCall(request).execute();
            String resp = response.body().string();
            try {
                JSONObject jj =  new JSONObject(resp);
                JSONArray data = jj.getJSONArray("data");
                for(int i=0;i<data.length();++i){
                    list.add(new News(data.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void getScholars(ArrayList<Scholar> scholars) {
        final String url = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";

        Request request = new Request.Builder().url(url).build();
        try {
            final Response response = client.newCall(request).execute();
            String resp = response.body().string();
            try {
                JSONObject json = new JSONObject(resp);
                if (!json.getString("message").equals("success")) {
                    return;
                }
                JSONArray scholarsArray = json.getJSONArray("data");
                for (int i = 0; i < scholarsArray.length(); ++i) {
                    scholars.add(new Scholar(scholarsArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
