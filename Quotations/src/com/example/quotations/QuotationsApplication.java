package com.example.quotations;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.QuotationTable;

public class QuotationsApplication extends Application {
    private static final String YOUR_APPLICATION_ID = "T7ERQGdeM8rlb00GTt0up3ioa5JQQmOLafINESSa";
    private static final String YOUR_CLIENT_KEY = "qqbo4C2Fw6EqYOWIs5yLN6zevLJQ2xMLyIzS23Ek";


    @Override
    public void onCreate() {
        super.onCreate();
        initParse();
    }

    private void initParse() {
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
