package com.viewpagerindicator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quotations.R;


public final class GuideFragment extends Fragment {
    private static final String KEY_CONTENT = "GuideFragment:Content";

    public static GuideFragment newInstance(int resourceId, boolean isLastPage) {
        GuideFragment fragment = new GuideFragment();

        fragment.mResourceId = resourceId;

        return fragment;
    }

    private int mResourceId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mResourceId = savedInstanceState.getInt(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv = inflater.inflate(mResourceId, container, false);

        Typeface SanchezFont = Typeface.createFromAsset(container.getContext().getAssets(), "fonts/sanchez.ttf");
        Typeface Robotoregular = Typeface.createFromAsset(container.getContext().getAssets(), "fonts/robotoregular.ttf");

        TextView tv = ((TextView)vv.findViewById(R.id.textViewGuideTitle));
        if(tv != null)
            tv.setTypeface(SanchezFont);
        tv = ((TextView)vv.findViewById(R.id.textViewGuideDescription));
        if(tv != null)
            tv.setTypeface(Robotoregular);

        return vv;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, mResourceId);
    }
}