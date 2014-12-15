package com.viewpagerindicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.quotations.R;

public class GuideFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private int mCount = 3;

    public GuideFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return GuideFragment.newInstance(R.layout.layout_guide_page1, false);
        else if (position == 1)
            return GuideFragment.newInstance(R.layout.layout_guide_page2, false);
        else
            return GuideFragment.newInstance(R.layout.layout_guide_page3, false);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title";
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}