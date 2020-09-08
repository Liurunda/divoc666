package com.java.liurunda.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.java.liurunda.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Scholar implements Serializable {
    public String name, nameZh, avatarUrl, id;

    public double activity, diversity, newStar, risingStar, sociability;
    public int citations, gIndex, hIndex, publications;

    public int numFollowed, numViewed;

    public String affiliation, bio, edu, position;

    public ArrayList<String> tags;
    public ArrayList<Integer> tagsScore;

    public boolean isPassedAway;
    public Scholar(){
        this.name ="name";
        this.nameZh = "name_zh";
    }
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
        }

        final JSONArray tags = json.optJSONArray("tags");
        if (tags != null) {
            for (int i = 0; i < tags.length(); ++i) {
                this.tags.add(tags.optString(i, ""));
            }
        }

        final JSONArray score = json.optJSONArray("tags_score");
        if (score != null) {
            for (int i = 0; i < score.length(); ++i) {
                this.tagsScore.add(score.optInt(i, 0));
            }
        }

        this.isPassedAway = json.optBoolean("is_passedaway", false);
    }

    public Bitmap getAvatarWrapper() {
        try {
            URL url = new URL(this.avatarUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();

            if (code == 200) {
                InputStream in = conn.getInputStream();
                return BitmapFactory.decodeStream(in);
            } else {
                return null;
            }
        } catch (Exception ignored) {
            return null;
        }
    }
}
