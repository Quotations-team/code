package com.example.quotations;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.Category;
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

/*
 * this Activity is called by splash screen.
 */
public class HomeActivity extends Activity {
    private final int remoteQueryRowsLimit = 50;
    private final int localStorageMaxRows = 200;
    String searchTerm = null;

    private ListviewAdapter adapter;
    private ListViewCategoryAdapter categoryAdapter;
    private List<Quotation> quotations;
    private List<Category> categories;


    private ListView listView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        handleIntent(getIntent());

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
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
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
        listView.setSelector(R.color.background_home);
        adapter = new ListviewAdapter(this, quotations);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadQuotes(getCategoryFilter(), searchTerm, page);
            }
        });
        loadQuotes(getCategoryFilter(), searchTerm, 1);
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

    private void loadQuotes(List<String> categories, String searchTerm, final int page) {
        ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
        query.setLimit(remoteQueryRowsLimit);

        if (categories != null) {
            String regex = "^.*(";
            for (String category : categories)
                regex += category + "|";
            if (regex.charAt(regex.length() - 1) == '|')
                regex = regex.substring(0, regex.length() - 1);
            regex += ").*$";
            query.whereMatches("Category", regex);
        }

        if (searchTerm != null) {
            String regex = "^";
            String[] words = searchTerm.trim().toLowerCase().split(" ");
            for (String word : words)
                regex += "(?=.*" + word + ")";

            query.whereMatches("Quote", regex);
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
                        updateQuoteListView(false);
                    } else {
                        quotations = data;
                        updateQuoteListView(true);
                    }
                }
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
            adapter = new ListviewAdapter(this, quotations);
            listView.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();
        showLoading(false);
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

    ProgressDialog progress;

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
        /*Intent i;
        switch (item.getItemId()) {
            case R.id.action_search:
                i = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(i);
                break;
            case R.id.action_category:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                else
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;//super.onCreateOptionsMenu(menu);
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