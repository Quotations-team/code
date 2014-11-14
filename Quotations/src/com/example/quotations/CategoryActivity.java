package com.example.quotations;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends Activity
{
	TextView tvSelectedCategory;
	List<QuotationTable> wholeQuotationTable, matchedCategoryQuotationTable;
	String strSelectedCategory, currentCategory;
	ListView listView;
	ArrayAdapter<QuotationTable> arrayAdapter;
	
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
		
		listView = (ListView) findViewById(R.id.lvMatchedCatQuote);
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
		arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, matchedCategoryQuotationTable);
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
			case R.id.miCatPageToHomePage:
				Toast.makeText(this, "Clicked Menu item launch HomeActivity", Toast.LENGTH_SHORT).show();
				Intent iMenuCatPageToHomePage = new Intent(getBaseContext(), HomeActivity.class);
				startActivity(iMenuCatPageToHomePage);
				break;
			case R.id.miCatPageToSrchPage:
				Toast.makeText(this, "Clicked Menu item launch SearchActivity", Toast.LENGTH_SHORT).show();
				Intent iMenuCatPageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
				startActivity(iMenuCatPageToSrchPage);
				break;
			case R.id.miSubCat_computer:
				Toast.makeText(this, "Clicked Sub Menu item launch CategoryActivity", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "computer";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_family:
				Toast.makeText(this, "Clicked Sub Menu item family", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "family";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_food:
				Toast.makeText(this, "Clicked Sub Menu item food", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "food";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_funny:
				Toast.makeText(this, "Clicked Sub Menu item funny", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "funny";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_inspirational:
				Toast.makeText(this, "Clicked Sub Menu item inspirational", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "inspirational";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_motivational:
				Toast.makeText(this, "Clicked Sub Menu item motivational", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "motivational";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_music:
				Toast.makeText(this, "Clicked Sub Menu item music", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "music";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_positive:
				Toast.makeText(this, "Clicked Sub Menu item positive", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "positive";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_smile:
				Toast.makeText(this, "Clicked Sub Menu item smile", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "smile";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
				break;
			case R.id.miSubCat_wisdom:
				Toast.makeText(this, "Clicked Sub Menu item wisdom", Toast.LENGTH_SHORT).show();
				strSelectedCategory = "wisdom";
				arrayAdapter.clear();
				displayMatchedCategoryQuote();
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
	
	public void onCatPageToHomePage(View v)
	{
		Toast.makeText(this, "Clicked Button launch HomeActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnCatPageToHomePage = new Intent(getBaseContext(), HomeActivity.class);
		startActivity(iBtnCatPageToHomePage);
	}
	
	public void onCatPageToSrchPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch SearchActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnCatPageToSrchPage = new Intent(getBaseContext(), SearchActivity.class);
		startActivity(iBtnCatPageToSrchPage);
	}
}
