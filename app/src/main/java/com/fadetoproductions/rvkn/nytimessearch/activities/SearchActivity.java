package com.fadetoproductions.rvkn.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fadetoproductions.rvkn.nytimessearch.EndlessScrollListener;
import com.fadetoproductions.rvkn.nytimessearch.R;
import com.fadetoproductions.rvkn.nytimessearch.adapters.ArticleArrayAdapter;
import com.fadetoproductions.rvkn.nytimessearch.clients.ArticleClient;
import com.fadetoproductions.rvkn.nytimessearch.fragments.SettingsFragment;
import com.fadetoproductions.rvkn.nytimessearch.models.Article;
import com.fadetoproductions.rvkn.nytimessearch.utils.Reachability;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.gvResults) GridView gvResults;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;

    int page = 0;
    String query;



//    https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=e844336f8dca4d5e934d0cab5ff9cc89&q=android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setupViews();
    }

    public void setupViews() {
        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(articleArrayAdapter);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });


        gvResults.setOnScrollListener(new EndlessScrollListener(20, 1) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                page += 1;
                searchForTerm("android");
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentManager fm = getSupportFragmentManager();
                settingsFragment.show(fm, "settings_fragment");

                return true;
            }
        });

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForTerm(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            SettingsFragment settingsFragment = new SettingsFragment();
//            FragmentManager fm = getSupportFragmentManager();
//            settingsFragment.show(fm, "settings_fragment");
//
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void searchForTerm(String query) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
//        RequestParams params = new RequestParams();
//        params.put("api-key", "e844336f8dca4d5e934d0cab5ff9cc89");
//        params.put("page", page);
//        params.put("q", query);

        Reachability reach = new Reachability(this);
        if (!reach.checkAndHandleConnection()) {
            return;
        }

        ArticleClient articleClient = new ArticleClient();
        articleClient.setListener(new ArticleClient.ArticleClientListener() {
            @Override
            public void searchForTermSuccess(ArrayList<Article> resultArticles) {
                articles.addAll(resultArticles);
                articleArrayAdapter.addAll(resultArticles);
            }
        });
        articleClient.searchForTerm(query);


//        client.get(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                JSONArray articleJsonResults = null;
//                try {
//                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
//                    articles.addAll(Article.fromJsonArray(articleJsonResults));
//                    articleArrayAdapter.addAll(Article.fromJsonArray(articleJsonResults));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }
}
