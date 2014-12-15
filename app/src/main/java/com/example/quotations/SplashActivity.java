package com.example.quotations;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.parse.ParseUser;


public class SplashActivity extends TabActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    SharedPreferences wmbPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FirstRun", true);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (isFirstRun) {
            Intent i = new Intent(getBaseContext(), GuideActivity.class);
            startActivity(i);
            finish();
        }
        else {
            // Show Splash Screen
            setContentView(R.layout.layout_splash_screen);

            TabHost tabHost = getTabHost();

            // Tab for Signin
            TabHost.TabSpec signinspec = tabHost.newTabSpec("Signin");
            signinspec.setIndicator("Signin", getResources().getDrawable(R.drawable.custom_tab));
            Intent photosIntent = new Intent(this, SigninActivity.class);
            signinspec.setContent(photosIntent);

            // Tab for Signup
            TabHost.TabSpec signupspec = tabHost.newTabSpec("Signup");
            signupspec.setIndicator("Signup", getResources().getDrawable(R.drawable.custom_tab));
            Intent songsIntent = new Intent(this, SignupActivity.class);
            signupspec.setContent(songsIntent);

            // Adding all TabSpec to TabHost
            tabHost.addTab(signinspec);   // Adding photos tab
            tabHost.addTab(signupspec);   // Adding songs tab

            // Check user is logged in
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start the app main activity
                        Intent i = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(i);

                        finish();
                    }
                }, SPLASH_TIME_OUT);
            } else {
                ((LinearLayout) findViewById(R.id.signinForm)).setVisibility(View.VISIBLE);
            }
        }
    }

    public void skipSignin(View view) {
        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }
}