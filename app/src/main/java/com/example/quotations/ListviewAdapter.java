package com.example.quotations;

import java.util.List;

import com.parse.ParseUser;
import com.parse.starter.Favorite;
import com.parse.starter.Like;
import com.parse.starter.Quotation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ListviewAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater = null;
    List<Quotation> quotes;
    List<Like> likes;
    List<Favorite> favorites;

    public ListviewAdapter(Activity a, List<Quotation> quotes, List<Like> likes, List<Favorite> ff) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.quotes = quotes;
        this.likes = likes;
        favorites = ff;
    }

    public int getCount() {
        return quotes.size();
    }

    public Object getItem(int position) {
        return quotes.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void insertItems(List<Quotation> quotations) {
        quotes.addAll(quotations);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = inflater.inflate(R.layout.listview_item, null);

        // Find all views
        TextView quoteTextView = (TextView) vi.findViewById(R.id.quoteText);
        TextView authorTextView = (TextView) vi.findViewById(R.id.quoteAuthor);
        TextView statusTextView = (TextView) vi.findViewById(R.id.quoteStatus);
        LinearLayout categoryIconsLayout = (LinearLayout) vi.findViewById(R.id.thumbnails);
        Button likeButton = (Button) vi.findViewById(R.id.like);
        Button commentButton = (Button) vi.findViewById(R.id.comment);
        Button favoriteButton = (Button) vi.findViewById(R.id.addToFavorite);

        // Get categories of each quote and add icons
        String[] categories = quotes.get(position).getCategories();
        for (String category : categories) {
            ImageView categoryIcon = new ImageView(vi.getContext());
            int i = vi.getContext().getResources().getIdentifier("drawable/" + category.toLowerCase()
                    + "_grey", null, vi.getContext().getPackageName());
            categoryIcon.setImageResource(i);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
            layoutParams.setMargins(0, 0, 10, 0);
            categoryIcon.setLayoutParams(layoutParams);
            categoryIconsLayout.addView(categoryIcon);
        }

        // Set texts
        quoteTextView.setText(quotes.get(position).getQuote());
        authorTextView.setText("—" + quotes.get(position).getAuthor());

        String statusText = "";
        if (quotes.get(position).getLikes() > 0)
            statusText += formatNumber(quotes.get(position).getLikes()) + " Likes  ";
        if (quotes.get(position).getComments() > 0)
            statusText += formatNumber(quotes.get(position).getComments()) + " Comments";
        statusTextView.setText(statusText);

        if (likes != null) {
            for (int i = 0; i < likes.size(); i++) {
                Object likedQuote = ((Quotation)likes.get(i).getQuoteId()).getObjectId();
                Object currentQuote = quotes.get(position).getObjectId();
                if (likedQuote.equals(currentQuote)) {
                    likeButton.setText("Liked");
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_active, 0, 0, 0);
                }
            }
        }

        if(favorites != null) {
            for (int i = 0; i < favorites.size(); i++) {
                Object favoriteQuote = ((Quotation)favorites.get(i).getQuoteId()).getObjectId();
                Object currentQuote = quotes.get(position).getObjectId();
                if (favoriteQuote.equals(currentQuote)) {
                    favoriteButton.setText("Added");
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_active, 0, 0, 0);
                }
            }
        }


        // Set Like button event
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null)
                    new FavoriteQuote().execute(currentUser, v);
                else
                    showRegistrationMessage(v.getContext());
            }
        });


        // Set Like button event
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null)
                    new LikeQuote().execute(currentUser, v);
                else
                    showRegistrationMessage(v.getContext());
            }
        });


        likeButton.setTag(quotes.get(position));
        commentButton.setTag(quotes.get(position));
        favoriteButton.setTag(quotes.get(position));
        return vi;
    }

    public void showRegistrationMessage(Context c) {
        new AlertDialog.Builder(c)
                .setTitle("Guess User")
                .setMessage("Please register to use all features.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private String formatNumber(int number)
    {
        String formattedNumber;
        if(number > 1000000)
            formattedNumber = String.format("%.1fM", number/ 1000000.0);
        else if (number > 1000)
            formattedNumber = String.format("%.1fK", number/ 1000.0);
        else
            formattedNumber = Integer.toString(number);

        return formattedNumber;
    }


}