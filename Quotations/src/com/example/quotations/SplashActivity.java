package com.example.quotations;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.quotations.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.GuideFragmentAdapter;

import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class SplashActivity extends FragmentActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences wmbPreference;
    CirclePageIndicator circlePageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FirstRun", true);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (isFirstRun) {
            // if its first lunch, open guide view
            setContentView(R.layout.activity_guide);

            //Set the pager with an adapter
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(new GuideFragmentAdapter(getSupportFragmentManager()));

            //Bind the title indicator to the adapter
            circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
            circlePageIndicator.setViewPager(pager);

            ((TextView)findViewById(R.id.skip)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endGuideView(null);
                }
            });

            circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (position == 2) {
                        TextView tx = (TextView) findViewById(R.id.skip);
                        tx.setVisibility(View.GONE);
                        Button btn = (Button) findViewById(R.id.endGuide);
                        btn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
            });

            if (false) {
                SharedPreferences.Editor editor = wmbPreference.edit();
                editor.putBoolean("FirstRun", false);
                editor.commit();
            }
        } else {

            // Show Splash Screen
            setContentView(R.layout.splash_screen);

            new Handler().postDelayed(new Runnable() {
                /*
                 * Showing splash screen with a timer.
                */
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start the app main activity
                    Intent i = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(i);

                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    public void endGuideView(View view) {
        SharedPreferences.Editor editor = wmbPreference.edit();
        editor.putBoolean("FirstRun", false);
        editor.commit();

        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }

}