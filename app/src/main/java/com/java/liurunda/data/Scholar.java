package com.java.liurunda.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Scholar {
    public String name, nameZh, avatarUrl, id;

    public double activity, diversity, newStar, risingStar, sociability;
    public int citations, gIndex, hIndex, publications;

    public int numFollowed, numViewed;

    public String affiliation, bio, edu, position;

    public ArrayList<String> tags;
    public ArrayList<Integer> tagsScore;

    public boolean isPassedAway;

    public Scholar(final JSONObject json) {
        this.name = json.optString("name", "");
        this.nameZh = json.optString("name_zh", "");
        this.avatarUrl = json.optString("avatar", "");
        this.id = json.optString("id", "");

        final JSONObject indices = json.optJSONObject("indices");
        if (indices != null) {
            this.activity = indices.optDouble("activity", 0.0);
            this.citations = indices.optInt("citations", 0);
            this.diversity = indices.optDouble("diversity", 0.0);
            this.gIndex = indices.optInt("gindex", 0);
            this.hIndex = indices.optInt("hindex", 0);
            this.newStar = indices.optDouble("newStar", 0.0);
            this.publications = indices.optInt("pubs", 0);
            this.risingStar = indices.optDouble("risingStar", 0.0);
            this.sociability = indices.optDouble("sociability", 0.0);
        }

        this.numFollowed = json.optInt("num_followed", 0);
        this.numViewed = json.optInt("num_viewed", 0);

        this.tags = new ArrayList<>();
        this.tagsScore = new ArrayList<>();

        final JSONObject profile = json.optJSONObject("profile");
        if (profile != null) {
            this.affiliation = profile.optString("affiliation", "");
            this.bio = profile.optString("bio", "");
            this.edu = profile.optString("edu", "");
            this.position = profile.optString("position", "");

            final JSONArray tags = profile.optJSONArray("tags");
            if (tags != null) {
                for (int i = 0; i < tags.length(); ++i) {
                    this.tags.add(tags.optString(i, ""));
                }
            }

            final JSONArray score = profile.optJSONArray("tags_score");
            if (score != null) {
                for (int i = 0; i < score.length(); ++i) {
                    this.tagsScore.add(score.optInt(i, 0));
                }
            }
        }

        this.isPassedAway = json.optBoolean("is_passedaway", false);
    }
}
