package com.fadetoproductions.rvkn.nytimessearch.clients;

import android.content.Context;
import android.util.Log;

import com.fadetoproductions.rvkn.nytimessearch.models.Article;
import com.fadetoproductions.rvkn.nytimessearch.utils.Reachability;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


public class ArticleClient {

    public interface ArticleClientListener {
        void searchSuccess(ArrayList<Article> articles);
    }

    private String API_KEY = "e844336f8dca4d5e934d0cab5ff9cc89";
    private String BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    public int RESULTS_PER_PAGE = 10;

    public void setListener(ArticleClientListener listener) {
        this.listener = listener;
    }

    private ArticleClientListener listener;
    Context context;

    public void setQuery(String query) {
        this.query = query;
    }

    public void nextPage() {
        page += 1;
    }

    String query;
    Date beginDate;
    Date endDate;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    String sort;
    Integer page;

    public ArticleClient(Context context) {
        this.context = context;
        this.listener = null;
        this.resetFilters();
    }

    public void resetFilters() {
        query = "";
        page = 1;
        sort = null;
        beginDate = null;
        endDate = null;
    }

    public void search() {
        Log.v("test", "searching");

        Reachability reach = new Reachability(context);
        if (!reach.checkAndHandleConnection()) {
            return;
        }

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

                    listener.searchSuccess(articleResults);
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
