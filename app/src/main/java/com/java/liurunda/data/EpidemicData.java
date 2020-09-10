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
    public void random(){
        overall.add(new EpidemicDataEntry(2,3,3,3));
        overall.add(new EpidemicDataEntry(2,3,3,3));
        overall.add(new EpidemicDataEntry(2,3,3,3));
        overall.add(new EpidemicDataEntry(2,3,3,3));
        overall.add(new EpidemicDataEntry(2,3,3,3));
        overall.add(new EpidemicDataEntry(2,3,3,3));
        regional.put("THU",new EpidemicDataEntry(2,3,3,3));
        regional.put("PKU",new EpidemicDataEntry(2,3,3,3));
        regional.put("ASD",new EpidemicDataEntry(2,3,3,3));
        regional.put("MIT",new EpidemicDataEntry(2,3,3,3));
        regional.put("as",new EpidemicDataEntry(2,3,3,3));
        regional.put("fuck",new EpidemicDataEntry(2,3,3,3));
    }
}
