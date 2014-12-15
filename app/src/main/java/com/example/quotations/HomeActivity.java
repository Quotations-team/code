package com.example.quotations;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Category;
import com.parse.starter.Favorite;
import com.parse.starter.Like;
import com.parse.starter.Quotation;

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

/*
 * this Activity is called by splash screen.
 */
public class HomeActivity extends Activity {

    private final int remoteQueryRowsLimit = 50;
    private final int localStorageMaxRows = 200;
    private String searchTerm = null;
    private ParseUser currentUser;

    private List<Quotation> quotations;
    private List<Category> categories;

    private ListviewQuoteAdapter adapter;
    private ListViewCategoryAdapter categoryAdapter;

    private ProgressDialog progress;
    private ListView listView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private int completedRequests = 0;
    private boolean refreshListView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_content);
        handleIntent(getIntent());

        currentUser = ParseUser.getCurrentUser();

        quotations = new ArrayList<Quotation>();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());


        // Init Dryer Navigation
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                   /* host Activity */
                mDrawerLayout,          /* DrawerLayout object */
                R.string.app_name,      /* "open drawer" description */
                R.string.app_name       /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Quotations");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Categories");
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        // Init Sidebar ListView
        mDrawerList = (ListView) findViewById(R.id.drawer_menu);

        final ParseQuery<Category> query = ParseQuery.getQuery("Category");
        query.orderByAscending("CategoryName");
        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> data, ParseException e) {
                if (e == null) {
                    categories = data;
                    updateCategoryListView();
                }
            }
        });

        // Init Quotes ListView
        listView = (ListView) findViewById(R.id.lvRandomQuote);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        listView.setSelector(R.color.background_home);
        adapter = new ListviewQuoteAdapter(this, quotations);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadQuotes(getCategoryFilter(), searchTerm, page);
            }
        });
        loadQuotes(getCategoryFilter(), searchTerm, 1);
    }

    private void loadQuotes(List<String> categories, String searchTerm, final int page) {

        ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
        query.setLimit(remoteQueryRowsLimit);
        query.orderByDescending("Likes");

        if (searchTerm != null && searchTerm.length() > 0) {
            String regex = "";
            String[] words = searchTerm.trim().toLowerCase().split(" ");
            for (String word : words)
                regex += "(?=.*\\W" + word + "\\W)";

            query.whereMatches("Quote", regex);
        }
        else if (categories != null && categories.size() > 0) {
            String regex = "^.*(";
            for (String category : categories)
                regex += category + "|";
            if (regex.charAt(regex.length() - 1) == '|')
                regex = regex.substring(0, regex.length() - 1);
            regex += ").*$";
            query.whereMatches("Category", regex);
        }


        if (page > 1) {
            query.setLimit(remoteQueryRowsLimit);
            query.setSkip((page - 1) * remoteQueryRowsLimit);
        }


        showLoading(true);

        query.findInBackground(new FindCallback<Quotation>() {


            @Override
            public void done(List<Quotation> data, ParseException e) {
                if (e == null) {

                    if (page > 1) {
                        for (int index = data.size() - 1; index >= 0; index--) {
                            if (!quotations.contains(data.get(index))) {
                                quotations.add(data.get(index));
                                if (quotations.size() > localStorageMaxRows)
                                    quotations.remove(quotations.size() - 1);
                            }
                        }
                        refreshListView = false;
                    } else {
                        quotations = data;
                        refreshListView = true;
                    }

                    // Load favorites and likes to show if layout_single_quote is liked or is in favorite list
                    if (currentUser != null) {
                        completedRequests = 0;
                        loadFavorites();
                        loadLikes();
                    }
                    else
                    {
                        updateQuoteListView(refreshListView);
                    }
                }
            }
        });
    }

    public void loadLikes(){
        ParseQuery<Like> likeQuery = ParseQuery.getQuery("Like");
        likeQuery.whereEqualTo("user", currentUser);
        likeQuery.whereContainedIn("quote", quotations);
        likeQuery.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> data, ParseException e) {
                QuotationsApplication.likes = data;
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
                QuotationsApplication.favorites = data;
                completedRequests++;

                if(completedRequests == 2)
                    updateQuoteListView(refreshListView);
            }
        });
    }

    public List<String> getCategoryFilter() {
        List<String> categoryFilter = new ArrayList<String>();
        int len = mDrawerList.getCount();
        SparseBooleanArray checked = mDrawerList.getCheckedItemPositions();

        if (checked != null) {
            for (int i = 0; i < len; i++)
                if (checked.get(i))
                    categoryFilter.add(categories.get(i).toString());
            return categoryFilter;
        } else
            return null;
    }

    public void updateQuoteListView(boolean refresh) {
        if (refresh) {
            adapter = new ListviewQuoteAdapter(this, quotations);
            listView.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();
        showLoading(false);
        if(listView.getCount() == 0)
            ((TextView)findViewById(R.id.emptyElement)).setText(getText(R.string.no_result_found));
    }

    public void updateCategoryListView() {
        categoryAdapter = new ListViewCategoryAdapter(this, categories);
        mDrawerList.setAdapter(categoryAdapter);

        // Click event for single list row
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadQuotes(getCategoryFilter(), null, 1);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.favorites:
                startActivity(new Intent(getBaseContext(), ActivityFavorites.class));
                break;
            case R.id.logout:
            case R.id.register:
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(getBaseContext(), SplashActivity.class));
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(currentUser == null)
            getMenuInflater().inflate(R.menu.home_guess_menu, menu);
        else
            getMenuInflater().inflate(R.menu.home_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            searchTerm = query;

            loadQuotes(getCategoryFilter(), searchTerm, 1);
            findViewById(R.id.action_search).clearFocus();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}