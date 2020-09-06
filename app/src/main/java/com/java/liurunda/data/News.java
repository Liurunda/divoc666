package com.java.liurunda.data;

import androidx.lifecycle.ViewModel;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@Entity
public class News implements Serializable {
    @PrimaryKey @NotNull
    public String id;

    public int infoType;
    public String title;
    public String content;
    public String source;
    public String origin_url;
    public String datetime;
    public int haveread;
    public String next_id, prev_id;
    ArrayList<String> keywords = new ArrayList<>(); // need type converter
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
            haveread = 0;
            JSONArray a = J.getJSONArray("entities");
            for(int i=0;i<a.length();++i) {
                keywords.add(a.getJSONObject(i).getString("label"));
            }
            next_id = ""; prev_id = "";
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    @NotNull
    @Override
    public String toString(){
        return title + " " + datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return id.equals(news.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public News merge_with_older(News older_record,boolean[] flag){
        flag[0] = (this.haveread != older_record.haveread);
        this.haveread = older_record.haveread;
        if(this.prev_id.equals("")){
            this.prev_id = older_record.prev_id;
            flag[0] = true;
        }
        if(this.next_id.equals("")){
            this.next_id = older_record.next_id;
            flag[0] = true;
        }
        return this;
    }
}
