package com.fadetoproductions.rvkn.nytimessearch.clients;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.fadetoproductions.rvkn.nytimessearch.models.Article;
import com.fadetoproductions.rvkn.nytimessearch.utils.Reachability;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


public class ArticleClient {

    public interface ArticleClientListener {
        void onSearch();
        void searchSuccess(ArrayList<Article> articles);
    }

    private String API_KEY = "e844336f8dca4d5e934d0cab5ff9cc89";
    private String BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMDD");
    public int RESULTS_PER_PAGE = 10;

    private ArticleClientListener listener;
    public ArrayList<String> topics;
    public Date beginDate;
    public Date endDate;
    Context context;
    String query;
    String sort;
    Integer page;

    public Boolean getOutOfResults() {
        return outOfResults;
    }

    Boolean outOfResults;

    public void setListener(ArticleClientListener listener) {
        this.listener = listener;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    public void resetPage() {
        page = 1;
    }
    public void nextPage() {
        page += 1;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

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
        topics = new ArrayList<>();
        outOfResults = false;
    }

    public void search() {
        listener.onSearch();
        Reachability reach = new Reachability(context);
        if (!reach.checkAndHandleConnection()) {
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = getRequestParams();

        Log.v("network-calls", "searching");
        Log.v("network-calls", "params: " + params.toString());

        client.get(BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    ArrayList<Article> articleResults = Article.fromJsonArray(articleJsonResults);
                    if (articleResults.isEmpty()) {
                        outOfResults = true;
                    } else {
                        listener.searchSuccess(articleResults);
                    }
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
        if (!query.isEmpty()) {
            params.put("q", query);
        }
        if (!topics.isEmpty()) {
            String newsDeskQuery = "news_desk:(" + TextUtils.join(" ", topics) + ")";
            params.put("fq", newsDeskQuery);
        }
        if (sort != null) {
            params.put("sort", sort);
        }
        if (beginDate != null) {
            params.put("begin_date", dateFormat.format(beginDate.getTime()));
        }
        if (endDate != null) {
            params.put("end_date", dateFormat.format(endDate.getTime()));
        }
        return params;
    }


}
