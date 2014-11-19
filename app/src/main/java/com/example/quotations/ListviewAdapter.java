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
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.listview_item, null);

        TextView category = (TextView) vi.findViewById(R.id.quoteItemCategory);
        TextView quote = (TextView) vi.findViewById(R.id.quoteItemQuote);

        category.setText(data.get(position).getCategory());
        quote.setText(data.get(position).getQuote());

        return vi;
    }
}