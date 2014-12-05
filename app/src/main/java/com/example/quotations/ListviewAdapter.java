package com.example.quotations;

import java.util.List;

import com.parse.starter.Category;
import com.parse.starter.Quotation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ListviewAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater = null;
    List<Quotation> data;

    public ListviewAdapter(Activity a, List<Quotation> quotes) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = quotes;
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

    public void insertItems(List<Quotation> quotations) {
        data.addAll(quotations);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = inflater.inflate(R.layout.listview_item, null);

        TextView author = (TextView) vi.findViewById(R.id.quoteAuthor);
        TextView quote = (TextView) vi.findViewById(R.id.quoteText);

        LinearLayout thumbnails = (LinearLayout)vi.findViewById(R.id.thumbnails);

        String[] categories = data.get(position).getCategories();
        for(String category: categories) {
            ImageView categoryIcon = new ImageView(vi.getContext());
            int i = vi.getContext().getResources().getIdentifier("drawable/" + category.toLowerCase()
                    + "_grey", null, vi.getContext().getPackageName());
            categoryIcon.setImageResource(i);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
            layoutParams.setMargins(0, 0, 10, 0);
            categoryIcon.setLayoutParams(layoutParams);
            thumbnails.addView(categoryIcon);
        }

        quote.setText(data.get(position).getQuote());
        author.setText("—" + data.get(position).getAuthor());

        return vi;
    }
}