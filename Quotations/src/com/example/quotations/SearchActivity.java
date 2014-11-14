package com.example.quotations;

import java.util.ArrayList;
import java.util.List;

import com.example.quotations.R;
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
import android.widget.Toast;

public class SearchActivity extends Activity
{
	EditText etSearchKeyWord;
	List<QuotationTable> wholeQuotationTable, matchedQuotationTable;
	String currentQuote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		init();
		queryData();
	}
	
	public void init()
	{
		etSearchKeyWord = (EditText) findViewById(R.id.etSearchKeyWord);
		etSearchKeyWord.setText("");
		wholeQuotationTable = new ArrayList<QuotationTable>();
		matchedQuotationTable = new ArrayList<QuotationTable>();
		currentQuote = "";
	}
	
	public void queryData() {
          ParseQuery<QuotationTable> query = ParseQuery.getQuery("QuotationTable");
          query.orderByAscending("ID");
          query.findInBackground(new FindCallback<QuotationTable>() {
              @Override
              public void done(List<QuotationTable> quotations, ParseException e) {
                  if (e != null) {
                      Toast.makeText(getBaseContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                  } else {
                      for (QuotationTable quotation : quotations) {
                          QuotationTable newQuote = new QuotationTable();
                          newQuote.setID(quotation.getID());
                          newQuote.setCategory(quotation.getCategory());
                          newQuote.setQuote(quotation.getQuote());
                          wholeQuotationTable.add(newQuote);
                      }
                  }
              }
          });
      }


	public void onSearchButtonClicked(View v)
	{
		for(int i=0; i<wholeQuotationTable.size(); i++)
		{
			currentQuote = wholeQuotationTable.get(i).getQuote();
			//this include search key as part of a word
//			if(currentQuote.contains(etSearchKeyWord.getText().toString()))
			//this search only whole exact key only
			if(currentQuote.matches(".*\\b" + etSearchKeyWord.getText().toString() + "\\b.*"))
			{
				matchedQuotationTable.add(wholeQuotationTable.get(i));
			}
		}

		ListView listView = (ListView) findViewById(R.id.lvMatchedQuote);
		ArrayAdapter<QuotationTable> arrayAdapter = new ArrayAdapter<QuotationTable>(getBaseContext(), android.R.layout.simple_list_item_1, matchedQuotationTable);
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
			case R.id.miSrchPageToHomePage:
				Toast.makeText(this, "Clicked Menu item launch HomeActivity", Toast.LENGTH_SHORT).show();
				Intent iMenuSrchPageToHomePage = new Intent(getBaseContext(), HomeActivity.class);
				startActivity(iMenuSrchPageToHomePage);
				break;
			case R.id.miSrchPageToCatPage:
				Toast.makeText(this, "Clicked Menu item launch CategoryActivity", Toast.LENGTH_SHORT).show();
				Intent iMenuSrchPageToCatPage = new Intent(getBaseContext(), CategoryActivity.class);
				startActivity(iMenuSrchPageToCatPage);
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
		getMenuInflater().inflate(R.menu.search_menu, menu);
	    return true;
	}
	
	public void onSrchPageToHomePage(View v)
	{
		Toast.makeText(this, "Clicked Button launch HomeActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnSrchPageToHomePage = new Intent(getBaseContext(), HomeActivity.class);
		startActivity(iBtnSrchPageToHomePage);
	}
	
	public void onSrchPageToCatPage(View v)
	{
		Toast.makeText(this, "Clicked Button launch CategoryActivity", Toast.LENGTH_SHORT).show();
		Intent iBtnSrchPageToCatPage = new Intent(getBaseContext(), CategoryActivity.class);
		startActivity(iBtnSrchPageToCatPage);
	}
}
