package com.fadetoproductions.rvkn.nytimessearch.clients;

import android.util.Log;

import com.fadetoproductions.rvkn.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rnewton on 8/9/16.
 */
public class ArticleClient {



    public interface ArticleClientListener {
        void searchForTermSuccess(ArrayList<Article> articles);
    }

    public enum SortOptions {
        NEWEST, OLDEST
    }

    private String API_KEY = "e844336f8dca4d5e934d0cab5ff9cc89";
    private String BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    public void setListener(ArticleClientListener listener) {
        this.listener = listener;
    }

    private ArticleClientListener listener;
    String query;
    Date beginDate;
    Date endDate;
    String sort;
    Integer page;

    public ArticleClient() {
        this.listener = null;
        query = "";
        page = 1;
        sort = null;
        beginDate = null;
        endDate = null;
    }


    public void searchForTerm(String query) {
        this.query = query;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = getRequestParams();



        client.get(BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    ArrayList<Article> articleResults = Article.fromJsonArray(articleJsonResults);

                    Log.v("fff", "dsafadsfdafds");

                    listener.searchForTermSuccess(articleResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private RequestParams getRequestParams() {
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", page);
        params.put("q", query);

        if (sort != null) {
            params.put("sort", sort);
        }

        // TODO begin date stuff

        return params;
    }


}
