package com.java.liurunda.data;

import java.util.ArrayList;

public class ScholarGetter {
    NetClient client = new NetClient();
    static ScholarGetter Getter;

    private ScholarGetter() {}

    public static ScholarGetter getInstance() {
        if (Getter == null) {
            Getter = new ScholarGetter();
        }
        return Getter;
    }

    public ArrayList<Scholar> getScholars() { // must be called asynchronously
        ArrayList<Scholar> list = new ArrayList<>();
        client.getScholars(list);
        return list;
    }
}
