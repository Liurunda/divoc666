package com.java.liurunda.data;

import androidx.room.util.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EpidemicDataUtil {
    public static void removeRedundantEntries(HashMap<String, EpidemicData> all) {
        for (Map.Entry<String, EpidemicData> entry: all.entrySet()) {
            String key = entry.getKey();
            String[] seg = key.split("\\|");
            String cur = seg[0];
            for (int i = 0; i < seg.length - 1; ++i) {
                all.remove(cur);
                cur += "|" + seg[i + 1];
            }
        }
    }

    public static HashMap<String, EpidemicData> foldByCountry(HashMap<String, EpidemicData> all) {
        HashMap<String, EpidemicData> countries = new HashMap<>();
        for (Map.Entry<String, EpidemicData> entry: all.entrySet()) {
            String key = entry.getKey();
            EpidemicData data = entry.getValue();
            String country = key.split("\\|", 2)[0];
            if (countries.containsKey(country)) {
                countries.put(country, countries.get(country).add(data));
            } else {
                countries.put(country, data);
            }
        }
        return countries;
    }

    public static HashMap<String, EpidemicData> foldByRegion(HashMap<String, EpidemicData> all) {
        HashMap<String, EpidemicData> regions = new HashMap<>();
        for (Map.Entry<String, EpidemicData> entry: all.entrySet()) {
            String key = entry.getKey();
            EpidemicData data = entry.getValue();
            String[] seg = key.split("\\|", 3);
            String region = seg[0] + "|" + seg[1];
            if (regions.containsKey(region)) {
                regions.put(region, regions.get(region).add(data));
            } else {
                regions.put(region, data);
            }
        }
        return regions;
    }

    public static HashMap<String, EpidemicData> fetchCountry(HashMap<String, EpidemicData> all, String country) {
        HashMap<String, EpidemicData> regions = new HashMap<>();
        for (Map.Entry<String, EpidemicData> entry: all.entrySet()) {
            String key = entry.getKey();
            EpidemicData data = entry.getValue();
            String[] seg = key.split("\\|", 3);
            if (seg[0].equals(country)) {
                regions.put(String.join("|", Arrays.copyOfRange(seg, 1, seg.length)), data);
            }
        }
        return regions;
    }
}
