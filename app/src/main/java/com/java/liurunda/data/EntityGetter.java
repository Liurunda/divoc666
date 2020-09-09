package com.java.liurunda.data;

import java.util.ArrayList;

public class EntityGetter {
    NetClient client = new NetClient();
    static EntityGetter Getter;

    private EntityGetter() {}

    public static EntityGetter getInstance() {
        if (Getter == null) {
            Getter = new EntityGetter();
        }
        return Getter;
    }
    public ArrayList<Entity> getEntities(String keyword) { // must be called asynchronously
        ArrayList<Entity> list = new ArrayList<>();
        client.getEntities(list, keyword);
        return list;
    }
}
