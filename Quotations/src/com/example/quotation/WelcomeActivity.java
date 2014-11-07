package com.example.quotation;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.QuotationTable;
import com.parse.starter.R;
import com.parse.starter.R.id;
import com.parse.starter.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class WelcomeActivity extends Activity
{
	List<QuotationTable> wholeQuotationTable, threeRandomQuotationTable;
	//this 3 int will change late on
	int rQuote1, rQuote2, rQuote3;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		init();
		queryData();
	}

	public void init()
	{
		wholeQuotationTable = new ArrayList<QuotationTable>();
		threeRandomQuotationTable = new ArrayList<QuotationTable>();
		rQuote1 = 0;
		rQuote2 = 1;
		rQuote3 = 2;
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
				generateRandomQuote();
			}
		});
	}

	public void generateRandomQuote()
	{
		rQuote1 = getRandomValue(0, wholeQuotationTable.size()-1);
		rQuote2 = getRandomValue(0, wholeQuotationTable.size()-1);
		rQuote3 = getRandomValue(0, wholeQuotationTable.size()-1);
		threeRandomQuotationTable.add(wholeQuotationTable.get(rQuote1));
		threeRandomQuotationTable.add(wholeQuotationTable.get(rQuote2));
		threeRandomQuotationTable.add(wholeQuotationTable.get(rQuote3));
		
		ListView listView = (ListView) findViewById(R.id.lvRandomQuote);
		//ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, wholeQuotationTable);
		ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, threeRandomQuotationTable);
		listView.setAdapter(arrayAdapter);
	}

	public int getRandomValue(int minimumRange, int maximumRange)
	{
		return((int)((maximumRange-minimumRange+1)*Math.random() + minimumRange));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{
			case R.id.miToSearchPage:
				Toast.makeText(this, "Clicked Menu item launch SearchActivity", Toast.LENGTH_SHORT).show();
				Intent iToSearchPage1 = new Intent(this, SearchActivity.class);
				startActivity(iToSearchPage1);
				break;
			case R.id.miToCategoryPage:
				Toast.makeText(this, "Clicked Menu item launch CategoryActivity", Toast.LENGTH_SHORT).show();
				Intent iToCategoryPage1 = new Intent(this, CategoryActivity.class);
				startActivity(iToCategoryPage1);
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
		getMenuInflater().inflate(R.menu.welcome_menu, menu);
	    return true;
	}
	
	public void onToSearchPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch SearchActivity", Toast.LENGTH_SHORT).show();
		Intent iToSearchPage2 = new Intent(getBaseContext(), SearchActivity.class);
		startActivity(iToSearchPage2);
	}
	
	public void onToCategoryPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch CategoryActivity", Toast.LENGTH_SHORT).show();
		Intent iToCategoryPage2 = new Intent(getBaseContext(), CategoryActivity.class);
		startActivity(iToCategoryPage2);
	}
}
