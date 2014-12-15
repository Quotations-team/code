package com.example.quotations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.starter.Favorite;
import com.parse.starter.Like;
import com.parse.starter.Quotation;

import java.util.List;


public class ListViewFavoriteAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater = null;

    List<Quotation> quotes;
    private List<Favorite> favorites;
    private List<Like> likes;

    public static Typeface SanchezFont;

    public ListViewFavoriteAdapter(Activity a, List<Quotation> quotes, List<Favorite> favorites, List<Like> likes) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.quotes = quotes;
        this.favorites = favorites;
        this.likes = likes;

        SanchezFont = Typeface.createFromAsset(a.getBaseContext().getAssets(), "fonts/sanchez.ttf");
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
        View vi = inflater.inflate(R.layout.listview_quote_item, null);

        // Find all views
        TextView quoteTextView = (TextView) vi.findViewById(R.id.quoteText);
        TextView authorTextView = (TextView) vi.findViewById(R.id.quoteAuthor);
        final TextView statusTextView = (TextView) vi.findViewById(R.id.quoteStatus);
        LinearLayout categoryIconsLayout = (LinearLayout) vi.findViewById(R.id.thumbnails);
        final Button likeButton = (Button) vi.findViewById(R.id.like);
        Button commentButton = (Button) vi.findViewById(R.id.comment);
        Button favoriteButton = (Button) vi.findViewById(R.id.addToFavorite);

        // Get categories of each layout_single_quote and add icons
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
        quoteTextView.setText("“" + quotes.get(position).getQuote() + "”");
        quoteTextView.setTypeface(SanchezFont);
        authorTextView.setText("—" + quotes.get(position).getAuthor());
        statusTextView.setText(QuotationsHelper.getStatusText(quotes.get(position)));

        // Set like button layout
        if (likes != null) {
            for (int i = 0; i < likes.size(); i++) {
                // if this quote is in user's like list
                String ss = ((Quotation)likes.get(i).getQuote()).getObjectId();
                String sg = quotes.get(position).getObjectId();
                if (ss.equals(sg))
                    LikeQuote.setLikeButtonLayout(likeButton, ButtonStatus.active);
            }
        }
        // Set Like button event
        likeButton.setOnClickListener(LikeQuote.onLikeClick);

        // Set favorite button layout
        if(favorites != null) {
            for (int i = 0; i < favorites.size(); i++) {
                // if this quote is in user's favorite list
                String ss = ((Quotation)favorites.get(i).getQuote()).getObjectId();
                String sg = quotes.get(position).getObjectId();
                if (ss.equals(sg))
                    FavoriteQuote.setFavoriteButtonLayout(favoriteButton, ButtonStatus.active);
            }
        }
        // Set like button event
        favoriteButton.setOnClickListener(FavoriteQuote.onFavoriteClick);

        // Set comment button event
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View commentButton) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null)
                {
                    Quotation quote = (Quotation)commentButton.getTag();
                    if(quote != null)
                    {
                        Intent i = new Intent(commentButton.getContext(), ActivitySingleQuote.class);
                        i.putExtra("quoteId", quote.getObjectId());
                        i.putExtra("quoteText", quote.getQuote());
                        i.putExtra("quoteAuthor", quote.getAuthor());
                        i.putExtra("quoteStatus", QuotationsHelper.getStatusText(quote));
                        commentButton.getContext().startActivity(i);
                    }
                }
                else {
                    QuotationsHelper.showAlertDialog(commentButton.getContext(),
                            commentButton.getContext().getString(R.string.ask_for_registration));
                }
            }
        });

        likeButton.setTag(quotes.get(position));
        commentButton.setTag(quotes.get(position));
        favoriteButton.setTag(quotes.get(position));
        return vi;
    }

}