package com.example.quotations;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.starter.*;

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
        ParseObject.registerSubclass(Quotation.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Favorite.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Like.class);

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        //Parse.enableLocalDatastore(this);

        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);  // Also in this method, specify a default Activity to handle push notifications
        PushService.setDefaultPushCallback(this, HomeActivity.class);

        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
