package com.java.liurunda.data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class News {
    String title;
    String content;
    String source;
    String origin_url;
    String datetime;
    ArrayList<String> keywords = new ArrayList<>();
    public News(){
    }
    public News(JSONObject J){
        try {
            title = J.getString("title");
            content = J.getString("content");
            source = J.getString("source");
            datetime = J.getString("date");
            origin_url = J.getJSONArray("urls").getString(0);
            JSONArray a = J.getJSONArray("entities");
            for(int i=0;i<a.length();++i){
                keywords.add(a.getJSONObject(i).getString("label"));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    @NotNull
    @Override
    public String toString(){
        return title + " " + datetime;
    }
}
