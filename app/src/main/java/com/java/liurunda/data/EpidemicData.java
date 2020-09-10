package com.java.liurunda.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class EpidemicData implements Serializable {
    public ArrayList<EpidemicDataEntry> overall = new ArrayList<>();
    public HashMap<String, EpidemicDataEntry> regional = new HashMap<>();

    public void reset() {
        overall.clear();
        regional.clear();
    }
}
