package com.example.quotations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.GuideFragmentAdapter;


public class GuideActivity extends FragmentActivity {

    SharedPreferences wmbPreference;
    CirclePageIndicator circlePageIndicator;
    Typeface SanchezFont;
    Typeface Helveticaneue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if its first lunch, open guide view
        setContentView(R.layout.layout_guide);

        //Set the pager with an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new GuideFragmentAdapter(getSupportFragmentManager()));

        //Bind the title indicator to the adapter
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(pager);

        ((TextView) findViewById(R.id.skip)).setOnClickListener(new View.OnClickListener() {
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
    }

    public void endGuideView(View view) {
        wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = wmbPreference.edit();
        editor.putBoolean("FirstRun", false);
        editor.commit();

        Intent i = new Intent(getBaseContext(), SplashActivity.class);
        startActivity(i);
        finish();
    }
}
