package com.example.quotations;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.starter.Category;

import java.util.List;


public class ListViewCategoryAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater = null;
    List<Category> data;

    public ListViewCategoryAdapter(Activity a, List<Category> categories) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = categories;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void insertItems() {

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.drawer_item, null);

        TextView category = (TextView) vi.findViewById(R.id.categoryName);

        category.setText(data.get(position).getCategory());

        return vi;
    }
}