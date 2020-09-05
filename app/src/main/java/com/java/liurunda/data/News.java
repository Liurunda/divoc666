package com.java.liurunda.data;

import androidx.lifecycle.ViewModel;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Entity
public class News {
    @PrimaryKey
    public int new_id;

    public String id;
    public int infoType;
    public String title;
    public String content;
    public String source;
    public String origin_url;
    public String datetime;

    ArrayList<String> keywords = new ArrayList<>();
    public News(){
    }
    public News(JSONObject J){
        try {
            title = J.getString("title");
            content = J.getString("content");
            source = J.getString("source");
            datetime = J.getString("date");
            origin_url = J.getJSONArray("urls").optString(0);
            infoType = InfoType.valueOf(J.getString("type")).ordinal();
            id = J.getString("_id");
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
