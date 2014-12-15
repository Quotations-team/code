package com.example.quotations;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.starter.Comment;

import java.util.Date;
import java.util.List;


public class ListViewCommentAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater = null;
    List<Comment> data;

    public ListViewCommentAdapter(Activity a, List<Comment> comments) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = comments;
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
            vi = inflater.inflate(R.layout.listview_comment_item, null);

        TextView commentUsername = (TextView) vi.findViewById(R.id.commentUsername);
        TextView commentText = (TextView) vi.findViewById(R.id.commentText);
        TextView commentTime = (TextView) vi.findViewById(R.id.commentTime);

        commentUsername.setText(data.get(position).getUserName());
        commentText.setText(data.get(position).getText());
        commentTime.setText(QuotationsHelper.getFormattedDate(data.get(position).getCreatedAt()));

        return vi;
    }

}