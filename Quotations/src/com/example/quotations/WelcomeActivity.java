package com.example.quotations;

import java.util.ArrayList;
import java.util.List;

import com.example.quotations.R;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class WelcomeActivity extends Activity
{
	List<QuotationTable> wholeQuotationTable, threeRandomQuotationTable;
	//this 3 int will change late on
	int rQuote1, rQuote2, rQuote3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
				//need to call it here, wait for fetch data,
				//if later on stored data on phone, can call it somewhere else
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
		Intent iMenuWelcPageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
		Intent iMenuWelcPageToCatPage = new Intent(getBaseContext(), CategoryActivity.class);
		
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{
			case R.id.miWelcPageToSrchPage:
				Toast.makeText(this, "Clicked Menu item launch SearchActivity", Toast.LENGTH_SHORT).show();
				startActivity(iMenuWelcPageToSrchPage);
				break;
			case R.id.miWelcPageToCatPage:
				Toast.makeText(this, "Clicked Menu item launch CategoryActivity", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "selected none");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_computer:
				Toast.makeText(this, "Clicked Sub Menu item launch CategoryActivity", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "computer");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_family:
				Toast.makeText(this, "Clicked Sub Menu item family", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "family");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_food:
				Toast.makeText(this, "Clicked Sub Menu item food", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "food");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_funny:
				Toast.makeText(this, "Clicked Sub Menu item funny", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "funny");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_inspirational:
				Toast.makeText(this, "Clicked Sub Menu item inspirational", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "inspirational");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_motivational:
				Toast.makeText(this, "Clicked Sub Menu item motivational", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "motivational");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_music:
				Toast.makeText(this, "Clicked Sub Menu item music", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "music");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_positive:
				Toast.makeText(this, "Clicked Sub Menu item positive", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "positive");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_smile:
				Toast.makeText(this, "Clicked Sub Menu item smile", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "smile");
				startActivity(iMenuWelcPageToCatPage);
				break;
			case R.id.miSubCat_wisdom:
				Toast.makeText(this, "Clicked Sub Menu item wisdom", Toast.LENGTH_SHORT).show();
				iMenuWelcPageToCatPage.putExtra("selected category", "wisdom");
				startActivity(iMenuWelcPageToCatPage);
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
	
	public void onWelcPageToSrchPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch SearchActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnWelcPageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
		startActivity(iBtnWelcPageToSrchPage);
	}
	
	public void onWelcPageToCatPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch CategoryActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnWelcPageToCatPage = new Intent(getBaseContext(), CategoryActivity.class);
		startActivity(iBtnWelcPageToCatPage);
	}
}
