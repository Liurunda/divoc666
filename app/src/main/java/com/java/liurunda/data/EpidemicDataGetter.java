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

    public HashMap<String, EpidemicData> getEpidemicData() { // must be called asynchronously
        HashMap<String, EpidemicData> data = new HashMap<>();
        client.getEpidemicData(data);
        EpidemicDataUtil.removeRedundantEntries(data);
        return data;
    }
}
