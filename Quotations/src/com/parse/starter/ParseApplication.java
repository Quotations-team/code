package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application
{
	//my ID and Key are from https://www.parse.com/apps/quickstart?onboard=#parse_data/mobile/android/native/new
	private static final String YOUR_APPLICATION_ID = "T7ERQGdeM8rlb00GTt0up3ioa5JQQmOLafINESSa";
	private static final String YOUR_CLIENT_KEY = "qqbo4C2Fw6EqYOWIs5yLN6zevLJQ2xMLyIzS23Ek";
	@Override
	public void onCreate()
	{
		super.onCreate();

		//register your subclass of ParseObject here, before initialize the app
		ParseObject.registerSubclass(QuotationTable.class);
		
		// Add your initialization code here
		Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}
}
