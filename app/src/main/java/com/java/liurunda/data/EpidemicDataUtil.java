package com.java.liurunda.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class EpidemicDataUtil {
    public static void parse(JSONObject json, EpidemicData domestic, EpidemicData global) {
        System.out.println("Entered parse");
        final int maximumDays = 10;
        domestic.reset();
        System.out.println("Domestic reset");
        global.reset();
        System.out.println("Global reset");
        try {
            JSONArray doverall = json.getJSONObject("China").getJSONArray("data");
            JSONArray goverall = json.getJSONObject("World").getJSONArray("data");
            System.out.println("Parsing phase 1: overall");
            for (int i = maximumDays; i > 0; --i) {
                if (i <= doverall.length()) {
                    JSONArray today = doverall.getJSONArray(doverall.length() - i);
                    domestic.overall.add(
                            new EpidemicDataEntry(today.optInt(0,0), today.optInt(1,0),
                                    today.optInt(2,0), today.optInt(3,0)));
                }
                if (i <= goverall.length()) {
                    JSONArray today = goverall.getJSONArray(goverall.length() - i);
                    global.overall.add(
                            new EpidemicDataEntry(today.optInt(0,0), today.optInt(1,0),
                                    today.optInt(2,0), today.optInt(3,0)));
                }
            }
            doverall = null;
            goverall = null;
            System.out.println("Parsing phase 2: regional");
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String key = it.next();
                String[] segments = key.split("\\|");
                if (segments.length == 2 && segments[0].equals("China")) {
                    JSONArray region = json.getJSONObject(key).getJSONArray("data");
                    JSONArray today = region.getJSONArray(region.length() - 1);
                    domestic.regional.put(segments[1], new EpidemicDataEntry(today.optInt(0,0), today.optInt(1,0),
                            today.optInt(2,0), today.optInt(3,0)));
                } else if (segments.length == 1) {
                    JSONArray region = json.getJSONObject(key).getJSONArray("data");
                    JSONArray today = region.getJSONArray(region.length() - 1);
                    global.regional.put(key, new EpidemicDataEntry(today.optInt(0,0), today.optInt(1,0),
                            today.optInt(2,0), today.optInt(3,0)));
                }
            }
        } catch (JSONException ignored) {
            ignored.printStackTrace();
        }

        System.out.println("Exiting parse");
    }
}
