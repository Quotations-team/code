package com.example.quotations;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.Category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CategoryActivity extends ActionBarActivity {
    String currentCategory;
    ListView listView;
    ListViewCategoryAdapter adapter;
    List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        currentCategory = "";

        listView = (ListView) findViewById(R.id.lvMatchedCatQuote);

        loadCategories();
    }

    public void loadCategories() {
        ParseQuery<Category> query = ParseQuery.getQuery("Category");

        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> data, ParseException e) {
                if (e == null) {
                    categories = data;
                    updateListView();
                }
            }
        });
    }

    public void updateListView() {
        adapter = new ListViewCategoryAdapter(this, categories);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.miCatPageToHomePage:
                Intent iMenuCatPageToHomePage = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(iMenuCatPageToHomePage);
                break;
            case R.id.miCatPageToSrchPage:
                Intent iMenuCatPageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(iMenuCatPageToSrchPage);
                break;
            case R.id.miSubCat_computer:
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_family:
                Toast.makeText(this, "Clicked Sub Menu item family", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_food:
                Toast.makeText(this, "Clicked Sub Menu item food", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_funny:
                Toast.makeText(this, "Clicked Sub Menu item funny", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_inspirational:
                Toast.makeText(this, "Clicked Sub Menu item inspirational", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_motivational:
                Toast.makeText(this, "Clicked Sub Menu item motivational", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_music:
                Toast.makeText(this, "Clicked Sub Menu item music", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_positive:
                Toast.makeText(this, "Clicked Sub Menu item positive", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_smile:
                Toast.makeText(this, "Clicked Sub Menu item smile", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            case R.id.miSubCat_wisdom:
                Toast.makeText(this, "Clicked Sub Menu item wisdom", Toast.LENGTH_SHORT).show();
                displayMatchedCategoryQuote();
                break;
            default:
                break;
        }
        return true;
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu, this add item to the action bar if it is present
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }
}
