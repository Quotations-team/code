package com.example.quotation;

import com.parse.starter.R;
import com.parse.starter.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class CategoryActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
	}
	
	public void onToWelcomePage(View v)
	{
		Toast.makeText(this, "Clicked Button launch WelcomeActivity", Toast.LENGTH_SHORT).show();
		Intent iToWelcomePage = new Intent(getBaseContext(), WelcomeActivity.class);
		startActivity(iToWelcomePage);
	}
}
