package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
/*
 * This app access parse.com server database to retrieve data and display it.
 * 
 * To do this app:
 * 1). Go to parse.com sign up and create a project get your project ID and key, download start project, test it.
 * 2). Create a table in parse.com.
 * 3). Query the data from your table and display it.
 */
public class ParseStarterProjectActivity extends Activity
{
	//this java list use to store data from parse.com server
	List<QuotationTable> quotationTable;
	EditText etSearchKeyWord;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ParseAnalytics.trackAppOpenedInBackground(getIntent());

		//		//NOT A GOOD IDEA TO CREATE DATABASE IN THE PROGRAM, BETTER DO IT ON PARSE.COM
		//		//ParseObject(className)		give the class name [table name].
		//		//create your own class extends ParseObject is better than using the object itself.
		//		ParseObject gameScore = new ParseObject("QuotationTable");
		//		//test save data to parse.com server
		//		//put(key, value)				give the field name and a data for that field.
		//		//saveInBackground()			save to parse.com database, https://www.parse.com/apps/quotation/collections
		//		//the database only update 1 saveInBackground() [the last one] per run of this app.
		//		gameScore.setObjectId(newObjectId)
		//		gameScore.put("Topic", "Funny");
		//		gameScore.put("Body", "Always remember that you are absolutely unique. Just like everyone else.");
		//		gameScore.saveInBackground();

		init();
		queryData();
	}

	public void init()
	{
		quotationTable = new ArrayList<QuotationTable>();
		etSearchKeyWord = (EditText) findViewById(R.id.etSearchKeyWord);
	}
	
	public void queryData()
	{
		//test retrieve list of data from parse.com server
		//can create ParseQuery object or use getQuery()
		//"QuotationTable" is our table
//		ParseQuery<QuotationTable> query = new ParseQuery<QuotationTable>("QuotationTable");
		ParseQuery<QuotationTable> query = ParseQuery.getQuery("QuotationTable");
		//order the display, by sorting a field
		query.orderByAscending("ID");
//		query.orderByDescending("ID");
		//set search category to include, by identify the field and its value
//		query.whereEqualTo("Topic", "food");
		//set search category to exclude, by exclude the field and its value
//		query.whereNotEqualTo("Topic", "food");
		//set search category to include more than 1 thing, by identify list of field values
		List<String> topices = new ArrayList<String>();
		topices.add("food");
		topices.add("Funny");
		query.whereContainedIn("Topic", topices);
		//set how many result to get back min 1, max 1000, default 100
//		query.setLimit(10);
		//skip the first 10 results
//		query.setSkip(10);
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
						newQuote.setTopic(quotation.getTopic());
						newQuote.setQuote(quotation.getQuote());
						quotationTable.add(newQuote);
					}
				}
				
				generateRandomQuote();
			}
		});
	}
	
	public void generateRandomQuote()
	{
		ListView listView = (ListView) findViewById(R.id.lvQuote);
		ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, quotationTable);
		listView.setAdapter(arrayAdapter);
	}
	
	public void onSearchButtonClicked(View v)
	{
		ListView listView = (ListView) findViewById(R.id.lvQuote);
		ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, quotationTable);
		listView.setAdapter(arrayAdapter);
	}
	public int getRandomValue(int minimumRange, int maximumRange)
	{
	   return((int)((maximumRange-minimumRange+1)*Math.random() + minimumRange));
	}
}