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
import android.widget.ProgressBar;

import com.fadetoproductions.rvkn.nytimessearch.EndlessScrollListener;
import com.fadetoproductions.rvkn.nytimessearch.R;
import com.fadetoproductions.rvkn.nytimessearch.adapters.ArticleArrayAdapter;
import com.fadetoproductions.rvkn.nytimessearch.clients.ArticleClient;
import com.fadetoproductions.rvkn.nytimessearch.fragments.SettingsFragment;
import com.fadetoproductions.rvkn.nytimessearch.models.Article;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SettingsFragment.SettingsDialogListener {

    @BindView(R.id.gvResults) GridView gvResults;
    @BindView(R.id.pbProgressAction) ProgressBar pbProgressAction;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;
    ArticleClient articleClient;

//    https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=e844336f8dca4d5e934d0cab5ff9cc89&q=android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setupArticleClient();
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

        gvResults.setOnScrollListener(new EndlessScrollListener(10, 1) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
//                if (!articleClient.getOutOfResults()) {
//                    return false;
//                }
                articleClient.nextPage();
                articleClient.search();
                return false;
            }
        });

        articleClient.search();
    }

    private void setupArticleClient() {
        articleClient = new ArticleClient(this);
        articleClient.setListener(new ArticleClient.ArticleClientListener() {
            @Override
            public void onSearch() {
                pbProgressAction.setVisibility(View.VISIBLE);
            }

            @Override
            public void searchSuccess(ArrayList<Article> resultArticles) {
                articles.addAll(resultArticles);
                pbProgressAction.setVisibility(View.INVISIBLE);
                articleArrayAdapter.addAll(resultArticles);
                articleArrayAdapter.notifyDataSetChanged();
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
                settingsFragment.articleClient = articleClient;
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
                articles = new ArrayList<>();
                articleArrayAdapter.clear();
                articleClient.setQuery(query);
                articleClient.resetPage();
                articleClient.search();
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
    public void onFinishDialog(Boolean changesMade) {
        // TODO both of these shouldn't need to be cleared
        articles.clear();
        articleArrayAdapter.clear();
        articleClient.resetPage();
        articleClient.search();
    }
}
