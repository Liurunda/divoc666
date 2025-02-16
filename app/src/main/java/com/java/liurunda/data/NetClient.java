package com.java.liurunda.data;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class getNewsList implements Callback{
    ArrayList<News> list;

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
    static OkHttpClient client =  new OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.MILLISECONDS)
            .build();
    final String NEWS_LIST = "https://covid-dashboard.aminer.cn/api/events/list";
    String news_list(InfoType infoType,int page, int pagesize){
        return NEWS_LIST + "?type=" + infoType.name()
                + "&page=" + page
                + "&size=" + pagesize;
    }
    public NetClient(){

    }
    public synchronized boolean getNews(ArrayList<News> list, InfoType t, int page, int size){
        Request request = new Request.Builder()
                .url(news_list(t,page,size))
                .build();
        try {
            final Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                return false;
            }
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
        return true;
    }
    public synchronized boolean getNewestNews(ArrayList<News> list, InfoType t, int size){ // 获取最新新闻时，总是进行网络请求(因为不知道当前数据库中是否最新)
        Request request = new Request.Builder()
                .url(news_list(t,1,size))
                .build();
        try {
            final Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String resp = response.body().string();

                try {
                    JSONObject jj = new JSONObject(resp);
                    JSONArray data = jj.getJSONArray("data");
                    for (int i = 0; i < data.length(); ++i) {
                        list.add(new News(data.getJSONObject(i)));
                    }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }else{
                return false;
            }
        }    catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

     boolean getScholars(ArrayList<Scholar> scholars) {
        final String url = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";

        scholars.clear();
        Request request = new Request.Builder().url(url).build();
        try {
            final Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            String resp = response.body().string();
            try {
                JSONObject json = new JSONObject(resp);
                if (!json.getString("message").equals("success")) {
                    return true;
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
        return true;
    }

     boolean getEpidemicData(EpidemicData domestic, EpidemicData global) {
        final String url = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";

        Request request = new Request.Builder().url(url).build();
        try {
            final Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            String resp = response.body().string();
            try {
               JSONObject json = new JSONObject(resp);
               EpidemicDataUtil.parse(json, domestic, global);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean getEntities(ArrayList<Entity> list,String keyword){
        final String entity_url_base = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=";
        Request request = new Request.Builder()
                .url(entity_url_base+keyword)
                .build();
        try {
            final Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                return false;
            }
            String resp = response.body().string();
            try {
                JSONObject jj =  new JSONObject(resp);
                JSONArray data = jj.getJSONArray("data");
                for(int i=0;i<data.length();++i){
                    list.add(new Entity(data.getJSONObject(i)));
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
