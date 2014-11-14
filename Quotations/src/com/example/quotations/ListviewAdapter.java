package com.example.quotations;

import java.util.ArrayList;
import java.util.HashMap;

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
        private ArrayList HashMap;
        String data;
        private static LayoutInflater inflater=null;
        //public ImageLoader imageLoader;

        public ListviewAdapter(Activity a, ArrayList<Quote> quotes) {
            activity = a;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //imageLoader=new ImageLoader(activity.getApplicationContext());
        }

        public int getCount() {
            return 5;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.listview_item, null);

            TextView title = (TextView)vi.findViewById(R.id.title); // title
            TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
            TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
            ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

            //HashMap&lt;String, String&gt; song = new HashMap&lt;String, String&gt;();
            //song = data.get(position);

            // Setting all values in listview
            title.setText("title");
            artist.setText("artist");
            duration.setText("duration");
            //imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
            return vi;
        }
    }