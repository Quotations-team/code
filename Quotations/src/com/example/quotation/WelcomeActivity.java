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
import android.os.Bundle;
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
	int[] threeQuoteIndexes;

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
		threeQuoteIndexes = new int[3];
	}

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
						newQuote.setTopic(quotation.getTopic());
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
//		do
//		{
//			
//		}
//		while(threeQuoteIndexes[0] != threeQuoteIndexes[1] != threeQuoteIndexes[2]);
//		rQuote1 = getRandomValue(0, wholeQuotationTable.size()-1);
//		rQuote2 = getRandomValue(0, wholeQuotationTable.size()-1);
//		rQuote3 = getRandomValue(0, wholeQuotationTable.size()-1);
//		threeRandomQuotationTable.add(object)
		ListView listView = (ListView) findViewById(R.id.lvRandomQuote);
		//ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, wholeQuotationTable);
		ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, wholeQuotationTable);
		listView.setAdapter(arrayAdapter);
	}

	public int getRandomValue(int minimumRange, int maximumRange)
	{
		return((int)((maximumRange-minimumRange+1)*Math.random() + minimumRange));
	}
}
