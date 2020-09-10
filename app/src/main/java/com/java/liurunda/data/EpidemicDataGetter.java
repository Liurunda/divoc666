package com.java.liurunda.data;

import java.util.HashMap;

public class EpidemicDataGetter {
    NetClient client = new NetClient();
    static EpidemicDataGetter Getter;

    private EpidemicDataGetter() {}

    public static EpidemicDataGetter getInstance() {
        if (Getter == null) {
            Getter = new EpidemicDataGetter();
        }
        return Getter;
    }

    public void getEpidemicData(EpidemicData domestic, EpidemicData global) { // must be called asynchronously
        client.getEpidemicData(domestic, global);
    }
}
