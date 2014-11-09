package com.example.quotations;

import java.util.ArrayList;
import java.util.List;

import com.example.quotations.R;
import com.example.quotations.R.layout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.QuotationTable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends Activity
{
	TextView tvSelectedCategory;
	List<QuotationTable> wholeQuotationTable, matchedCategoryQuotationTable;
	String strSelectedCategory, currentCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		init();
		
		tvSelectedCategory = (TextView) findViewById(R.id.tvSelectedCategory);
		Bundle bExtras = getIntent().getExtras();
		if(bExtras != null)
		{
			strSelectedCategory = bExtras.getString("selected category");
			tvSelectedCategory.setText(strSelectedCategory);
		}
		
		queryData();
		
	}
	
	public void init()
	{
		wholeQuotationTable = new ArrayList<QuotationTable>();
		matchedCategoryQuotationTable = new ArrayList<QuotationTable>();
		strSelectedCategory = "";
		currentCategory = "";
	}
	
	public void queryData()
	{
		ParseQuery<QuotationTable> query = ParseQuery.getQuery("QuotationTable");
		query.orderByAscending("ID");
		query.findInBackground(new FindCallback<QuotationTable>()
		{
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
					//need to call it here, wait for fetch data,
					//if later on stored data on phone, can call it somewhere else
					displayMatchedCategoryQuote();
				}
			}
		});
	}
	
	public void displayMatchedCategoryQuote()
	{
		for(int i=0; i<wholeQuotationTable.size(); i++)
		{
			currentCategory = wholeQuotationTable.get(i).getCategory();
			if(currentCategory.matches(".*\\b" + strSelectedCategory + "\\b.*"))
			{
				matchedCategoryQuotationTable.add(wholeQuotationTable.get(i));
			}
		}

		ListView listView = (ListView) findViewById(R.id.lvMatchedCatQuote);
		ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, matchedCategoryQuotationTable);
		listView.setAdapter(arrayAdapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{
			case R.id.miCatPageToWelcPage:
				Toast.makeText(this, "Clicked Menu item launch WelcomeActivity", Toast.LENGTH_SHORT).show();
				Intent iMenuCatPageToWelcPage = new Intent(getBaseContext(), WelcomeActivity.class);
				startActivity(iMenuCatPageToWelcPage);
				break;
			case R.id.miCatPageToSrchPage:
				Toast.makeText(this, "Clicked Menu item launch SearchActivity", Toast.LENGTH_SHORT).show();
				Intent iMenuCatPageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
				startActivity(iMenuCatPageToSrchPage);
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
		getMenuInflater().inflate(R.menu.category_menu, menu);
	    return true;
	}
	
	public void onCatPageToWelcPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch WelcomeActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnCatPageToWelcPage = new Intent(getBaseContext(), WelcomeActivity.class);
		startActivity(iBtnCatPageToWelcPage);
	}
	
	public void onCatPageToSrchPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch SearchActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnCatPageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
		startActivity(iBtnCatPageToSrchPage);
	}
}
