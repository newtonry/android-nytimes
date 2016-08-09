package com.fadetoproductions.rvkn.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rnewton on 8/8/16.
 */
public class Article implements Serializable {

    String webUrl;

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    String headline;
    String thumbnailImage;

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbnailImage = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbnailImage = "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJsonArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                Article article = new Article(array.getJSONObject(i));
                results.add(article);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

}
