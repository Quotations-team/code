package com.example.quotations;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.starter.Quotation;
import com.parse.FindCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
    EditText etSearchKeyWord;
    ListviewAdapter adapter;
    ListView listView;
    List<Quotation> matchedQuotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        matchedQuotation = new ArrayList<Quotation>();

        listView = (ListView) findViewById(R.id.lvMatchedQuote);
        listView.setEmptyView((TextView) findViewById(R.id.textViewEmptySearch));

        etSearchKeyWord = (EditText) findViewById(R.id.etSearchKeyWord);
        etSearchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    public void performSearch() {
        ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");

        // regex for or words: "^.*(word1|word2|word3).*$"
        // regex for and words: ^(?=.*word1)(?=.*word2)(?=.*p=word3)
        String regex = "^";
        String[] words = etSearchKeyWord.getText().toString().trim().toLowerCase().split(" ");
        for (String word : words)
            regex += "(?=.*" + word + ")";

        // This part is used for Or regex
        //if (regex.charAt(regex.length()-1)==')')
        //    regex = regex.substring(0, regex.length()-1);
        //regex += ").*$";

        query.whereMatches("Quote", regex);

        query.findInBackground(new FindCallback<Quotation>() {
            public void done(List<Quotation> data, ParseException e) {
                if (e == null) {
                    matchedQuotation = data;
                    etSearchKeyWord.clearFocus();
                    updateListView();
                }
            }
        });
    }

    public void updateListView() {
        adapter = new ListviewAdapter(this, matchedQuotation);
        listView.setAdapter(adapter);

        // Click event for single list row
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
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

    public void clearSearchTerm(View view) {
        etSearchKeyWord.setText("");
        etSearchKeyWord.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu, this add item to the action bar if it is present
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }
}
