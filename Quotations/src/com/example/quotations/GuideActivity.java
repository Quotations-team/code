package com.example.quotations;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.*;


public class GuideActivity extends FragmentActivity
{	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		 //Set the pager with an adapter
		 ViewPager pager = (ViewPager)findViewById(R.id.pager);		 
		 pager.setAdapter(new GuideFragmentAdapter(getSupportFragmentManager()));

		 //Bind the title indicator to the adapter
		 CirclePageIndicator circlePageIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		 circlePageIndicator.setViewPager(pager);
		
	}
}