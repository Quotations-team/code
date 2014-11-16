package com.example.quotations;

import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.Quotation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/*
 * this Activity is called by splash screen.
 */
public class HomeActivity extends Activity {
    private final int remoteQueryRowsLimit = 20;
    private final int localStorageMaxRows = 200;
    private final String FEED_LABEL = "FEED_LABEL";
    ListView listView;
    ListviewAdapter adapter;
    List<Quotation> quotations;
    private boolean autoLoadNewQuotes = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        listView = (ListView) findViewById(R.id.lvRandomQuote);
        listView.setSelector(R.color.background_home);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.miHomePageToSrchPage:
                i = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.miHomePageToCatPage:
                i = new Intent(getBaseContext(), CategoryActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu, this add item to the action bar if it is present
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

}