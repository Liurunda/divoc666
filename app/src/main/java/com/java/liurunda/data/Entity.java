package com.java.liurunda.data;

import android.util.Pair;
import android.util.Property;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Entity {

    public String name; //JSON.getString("label")
    public String img_url;// JSON.getString("img")
    public String enwiki; //JSON.abstractInfo.enwiki
    public String baidu;//JSON.abstractInfo.baidu
    public ArrayList<Pair<String,String>> properties = new ArrayList<>();
    public ArrayList<Pair<String,String>> relations = new ArrayList<>();
    public double hot; //(0, 1).
    Entity(String name){
        this.name = name;
    }
    Entity(JSONObject E){
        try {
            name = E.getString("label");
            hot = E.getDouble("hot");
            img_url = E.getString("img");

            E = E.getJSONObject("abstractInfo");
            enwiki = E.getString("enwiki");
            baidu = E.getString("baidu");
            E = E.getJSONObject("COVID");

            JSONObject prop = E.getJSONObject("properties");
            prop.keys().forEachRemaining(key->{
                properties.add(Pair.create(key,prop.optString(key,"")));
            });

            JSONArray rela = E.getJSONArray("relations");
            int L = rela.length();
            for(int i=0;i<L;++i){
                relations.add(Pair.create(rela.getJSONObject(i).getString("label"),rela.getJSONObject(i).getString("relation")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
