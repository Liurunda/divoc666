package com.java.liurunda.data;

import android.util.Property;

import java.util.ArrayList;

public class Entity {

    public String name; //JSON.getString("label")
    public String img_url;// JSON.getString("img")
    public String enwiki; //JSON.abstractInfo.enwiki
    public String baidu;//JSON.abstractInfo.baidu
    public ArrayList<Property<String,String>> properties;
    public double hot; //(0, 1).
}
