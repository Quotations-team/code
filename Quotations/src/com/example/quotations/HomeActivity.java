package com.example.quotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.example.quotations.R;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/*
 * this Activity is call by splash screen.
 * this also query all quotes from database only 1 time.
 * 		(don't know if any limit on query amount of data)
 * save all quotes in the user cell phone.
 * display 3 random quotes.
 */
public class HomeActivity extends Activity
{
	List<QuotationTable> wholeQuotationTable, randomQuotationTable;
	Set<Integer> tempUniqueTable;
	int tempIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_home);

          ParseAnalytics.trackAppOpenedInBackground(getIntent());
          init();
          queryData();
      }

	public void init()
	{
		wholeQuotationTable = new ArrayList<QuotationTable>();
		randomQuotationTable = new ArrayList<QuotationTable>();
		tempUniqueTable = new TreeSet<Integer>();
		tempIndex = 0;
	}

	//this query will be change later by only query here, only once and save all data in cell phone.
	//when we need to access the data again, we access the local stored data.
	public void queryData()
	{
		//retrieve list of data from parse.com server
		//can create ParseQuery object or use getQuery()
		//"QuotationTable" is our table
		//ParseQuery<QuotationTable> query = new ParseQuery<QuotationTable>("QuotationTable");
		ParseQuery<QuotationTable> query = ParseQuery.getQuery("QuotationTable");
		//order the display, by sorting a field
		query.orderByAscending("ID");
		//findInBackground() fetch ParseObject QuotationTable from database
		//it has its own thread
		query.findInBackground(new FindCallback<QuotationTable>()
		{
			//when fetch is done
			//List<QuotationTable> quotations will be our ParseObject from the parse.com database, our table
			@Override
			public void done(List<QuotationTable> quotations, ParseException e)
			{
				if (e != null)
				{
					Toast.makeText(getBaseContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
				}
				else
				{
					for(QuotationTable quotation : quotations)
					{
						QuotationTable newQuote = new QuotationTable();
						newQuote.setID(quotation.getID());
						newQuote.setCategory(quotation.getCategory());
						newQuote.setQuote(quotation.getQuote());
						wholeQuotationTable.add(newQuote);
					}
				}
				//need to call it here, wait for fetch data,
				//if later on stored data on phone, can call it somewhere else
				generateRandomQuote(4);
			}
		});
	}

	public void generateRandomQuote(int numOfRanQuote)
	{
		while(tempUniqueTable.size() < numOfRanQuote)
		{
			tempIndex = getRandomValue(0, wholeQuotationTable.size()-1);
			tempUniqueTable.add(tempIndex);
		}
		for(int i : tempUniqueTable)
		{
			randomQuotationTable.add(wholeQuotationTable.get(i));
		}
		
		ListView listView = (ListView) findViewById(R.id.lvRandomQuote);
		ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, randomQuotationTable);
		listView.setAdapter(arrayAdapter);
		listView.setBackgroundColor(Color.LTGRAY);
	}

	public int getRandomValue(int minimumRange, int maximumRange)
	{
		return((int)((maximumRange-minimumRange+1)*Math.random() + minimumRange));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent iMenuHomePageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
		Intent iMenuHomePageToCatPage = new Intent(getBaseContext(), CategoryActivity.class);
		
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{
			case R.id.miHomePageToSrchPage:
				Toast.makeText(this, "Clicked Menu item launch SearchActivity", Toast.LENGTH_SHORT).show();
				startActivity(iMenuHomePageToSrchPage);
				break;
			case R.id.miHomePageToCatPage:
				Toast.makeText(this, "Clicked Menu item launch CategoryActivity", Toast.LENGTH_SHORT).show();
				iMenuHomePageToCatPage.putExtra("selected category", "selected none");
				startActivity(iMenuHomePageToCatPage);
				break;
			default:
				break;
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    //inflate the menu, this add item to the action bar if it is present
		getMenuInflater().inflate(R.menu.home_menu, menu);
	    return true;
	}
	
	public void onHomePageToSrchPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch SearchActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnHomePageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
		startActivity(iBtnHomePageToSrchPage);
	}
	
	public void onHomePageToCatPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch CategoryActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnHomePageToCatPage = new Intent(getBaseContext(), CategoryActivity.class);
		startActivity(iBtnHomePageToCatPage);
	}
}
