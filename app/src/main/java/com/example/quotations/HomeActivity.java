package com.example.quotations;

import java.util.ArrayList;
import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.Category;
import com.parse.starter.Quotation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*
 * this Activity is called by splash screen.
 */
public class HomeActivity extends Activity {
    private final int remoteQueryRowsLimit = 20;
    private final int localStorageMaxRows = 200;
    private final String FEED_LABEL = "FEED_LABEL";
    ListView listView;
    ListviewAdapter adapter;
    ListViewCategoryAdapter categoryAdapter;
    List<Quotation> quotations;
    private boolean autoLoadNewQuotes = true;
    List<Category> categories;

    ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());


        // Init Dryer Navigation
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                                   /* host Activity */
                mDrawerLayout,                          /* DrawerLayout object */
                R.string.app_name,    /* nav drawer icon to replace 'Up' caret */
                R.string.app_name                      /* "open drawer" description */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("mTitle");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("mDrawerTitle");
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        ParseQuery<Category> query = ParseQuery.getQuery("Category");

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
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
                query.setLimit(remoteQueryRowsLimit);
                query.setSkip((page - 1) * remoteQueryRowsLimit);

                query.findInBackground(new FindCallback<Quotation>() {
                    @Override
                    public void done(List<Quotation> data, ParseException e) {
                        if (e == null) {
                            //adapter.insertItems(data);
                            quotations.addAll(data);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        loadFromDatabase();

        if (autoLoadNewQuotes) {
            autoLoadNewQuotes = false;
            loadNewQuotes();
        }

    }

    public void loadFromDatabase() {
        ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Quotation>() {
            public void done(final List<Quotation> data, ParseException e) {
                if (e == null) {
                    quotations = data;
                    updateListView();
                }
            }
        });
    }

    public void loadNewQuotes() {
        ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
        query.setLimit(remoteQueryRowsLimit);

        query.findInBackground(new FindCallback<Quotation>() {
            @Override
            public void done(List<Quotation> data, ParseException e) {
                if (e == null) {
                    for (int index = data.size() - 1; index >= 0; index--) {
                        if (!quotations.contains(data.get(index))) {
                            quotations.add(data.get(index));
                            if (quotations.size() > localStorageMaxRows)
                                quotations.remove(quotations.size() - 1);
                        }
                    }

                    // Release any objects previously pinned for this query.
                    Quotation.unpinAllInBackground(FEED_LABEL, quotations,
                            new DeleteCallback() {
                                public void done(ParseException e) {
                                    if (e != null) {
                                        // There was some error.
                                        return;
                                    }

                                    // Add the latest results for this query to the cache.
                                    Quotation.pinAllInBackground(FEED_LABEL, quotations);
                                }
                            });
                }
                updateListView();
            }
        });
    }

    public void updateListView() {
        adapter = new ListviewAdapter(this, quotations);

        listView.setAdapter(adapter);

        // Click event for single list row
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
    }

    public void updateCategoryListView() {
        categoryAdapter = new ListViewCategoryAdapter(this, categories);

        mDrawerList.setAdapter(categoryAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_search:
                i = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(i);
                break;
            case R.id.action_category:

                break;
            default:
                break;
        }
        return true;
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mDrawerLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        /*Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer_item
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);*/
    }

    @Override
    public void setTitle(CharSequence title) {
        /*mTitle = title;
        getActionBar().setTitle(mTitle);
        */
    }
}