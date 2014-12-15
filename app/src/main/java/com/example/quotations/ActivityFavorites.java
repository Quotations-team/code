package com.example.quotations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Category;
import com.parse.starter.Favorite;
import com.parse.starter.Like;
import com.parse.starter.Quotation;

import java.util.ArrayList;
import java.util.List;

/*
 * this Activity is called by splash screen.
 */
public class ActivityFavorites extends Activity {

    private List<Quotation> quotations;
    private List<Favorite> favorites;
    private List<Like> likes;

    private ListViewFavoriteAdapter adapter;

    private ListView listView;
    private ProgressDialog progress;

    ParseUser currentUser;
    private int completedRequests = 0;
    private boolean refreshListView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_content);

        currentUser = ParseUser.getCurrentUser();

        quotations = new ArrayList<Quotation>();

        // Init Quotes ListView
        listView = (ListView) findViewById(R.id.lvRandomQuote);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        listView.setSelector(R.color.background_home);
        loadFavoriteQuotes();
    }

    private void loadFavoriteQuotes() {

        ParseQuery<Favorite> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("user", currentUser);
        query.orderByDescending("Likes");

        showLoading(true);

        query.findInBackground(new FindCallback<Favorite>() {

            @Override
            public void done(List<Favorite> data, ParseException e) {
                if (e == null) {

                    List<String> ids = new ArrayList<String>();
                    for(Favorite fav:data) {
                        ids.add(((Quotation) fav.getQuote()).getObjectId());
                    }

                    ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
                    query.whereContainedIn("objectId", ids);
                    query.orderByDescending("Likes");
                    query.findInBackground(new FindCallback<Quotation>() {

                        @Override
                        public void done(List<Quotation> data, ParseException e) {
                            if (e == null) {
                                quotations = data;

                                refreshListView = true;

                                // Load favorites and likes to show if layout_single_quote is liked or is in favorite list
                                if (currentUser != null) {
                                    completedRequests = 0;
                                    loadFavorites();
                                    loadLikes();
                                } else {

                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void updateQuoteListView(boolean refresh) {
        if (refresh) {
            adapter = new ListViewFavoriteAdapter(this, quotations, favorites, likes);
            listView.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();
        showLoading(false);
        if(listView.getCount() == 0)
            ((TextView)findViewById(R.id.emptyElement)).setText(getString(R.string.no_favorite_found));
    }

    private void showLoading(boolean visible) {
        if (visible) {
            progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
        } else
            progress.dismiss();
    }

    public void loadLikes(){
        ParseQuery<Like> likeQuery = ParseQuery.getQuery("Like");
        likeQuery.whereEqualTo("user", currentUser);
        likeQuery.whereContainedIn("quote", quotations);
        likeQuery.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> data, ParseException e) {
                likes = data;
                completedRequests++;

                if (completedRequests == 2)
                    updateQuoteListView(refreshListView);
            }
        });
    }

    public void loadFavorites(){

        ParseQuery<Favorite> favoriteQuery = ParseQuery.getQuery("Favorite");
        favoriteQuery.whereEqualTo("user", currentUser);
        favoriteQuery.findInBackground(new FindCallback<Favorite>() {
            @Override
            public void done(List<Favorite> data, ParseException e) {
                favorites = data;
                completedRequests++;

                if(completedRequests == 2)
                    updateQuoteListView(refreshListView);
            }
        });
    }
}