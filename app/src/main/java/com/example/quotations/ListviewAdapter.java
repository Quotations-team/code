package com.example.quotations;

import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.Favorite;
import com.parse.starter.Like;
import com.parse.starter.Quotation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
        if(quotes.get(position).getLikes() > 0)
            statusText += formatNumber(quotes.get(position).getLikes()) + " Likes  ";
        if(quotes.get(position).getComments() > 0)
            statusText += formatNumber(quotes.get(position).getComments()) + " Comments";
        statusTextView.setText(statusText);

        for (int i = 0; i < likes.size(); i++) {
            if (likes.get(i).getQuoteId().equals(quotes.get(position))) {
                likeButton.setText("Liked");
                likeButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.like_active, 0, 0, 0);
            }
        }
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getQuoteId().equals(quotes.get(position))) {
                favoriteButton.setText("Added");
                favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_active, 0, 0, 0);
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


    class LikeQuote extends AsyncTask<Object, Boolean, Boolean> {

        protected void onPreExecute() {
        }

        Button btn;
        ParseUser currentUser;

        protected Boolean doInBackground(Object... params) {
            currentUser = (ParseUser) params[0];
            btn = (Button) params[1];

            ParseQuery<Like> likeQuery = ParseQuery.getQuery("Like");
            likeQuery.whereEqualTo("userId", currentUser);
            likeQuery.whereEqualTo("quoteId", btn.getTag());
            likeQuery.findInBackground(new FindCallback<Like>() {
                @Override
                public void done(List<Like> data, com.parse.ParseException e) {

                    if (data == null || data.size() == 0) {
                        // Create like object and save it in cloud
                        Like like = new Like();
                        like.put("userId", ParseUser.getCurrentUser());
                        like.put("quoteId", btn.getTag());
                        like.saveInBackground();

                        ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
                        String quoteId = ((Quotation)btn.getTag()).getObjectId();
                        // Retrieve the object by id
                        query.getInBackground(quoteId, new GetCallback<Quotation>() {
                            public void done(Quotation quote, com.parse.ParseException e) {
                                if (e == null) {
                                    quote.addLikes();
                                    quote.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(com.parse.ParseException e) {
                                            if (e == null) {
                                                // Saving like finished successfully
                                                // Change button text to liked
                                                btn.setText(btn.getContext().getString(R.string.like_active));
                                                btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_active, 0, 0, 0);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });

            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {

            }
        }

    }


    class FavoriteQuote extends AsyncTask<Object, Boolean, Boolean> {

        protected void onPreExecute() {
        }

        Button btn;
        ParseUser currentUser;

        protected Boolean doInBackground(Object... params) {
            currentUser = (ParseUser) params[0];
            btn = (Button) params[1];

            ParseQuery<Favorite> favoriteQuery = ParseQuery.getQuery("Favorite");
            favoriteQuery.whereEqualTo("userId", currentUser);
            favoriteQuery.whereEqualTo("quoteId", btn.getTag());
            favoriteQuery.findInBackground(new FindCallback<Favorite>() {
                @Override
                public void done(List<Favorite> data, com.parse.ParseException e) {
                    // If favorite does not exists
                    if (data == null || data.size() == 0) {
                        // Create like object and save it in cloud
                        Favorite favorite = new Favorite();
                        favorite.put("userId", ParseUser.getCurrentUser());
                        favorite.put("quoteId", btn.getTag());
                        favorite.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(com.parse.ParseException e) {
                                if(e==null) {
                                    // Saving favorite finished successfully
                                    String tt = btn.getContext().getString(R.string.favorite_active);
                                    btn.setText(tt);
                                    btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_active, 0, 0, 0);
                                }
                            }
                        });
                    }
                    else {
                        data.get(0).deleteEventually(new DeleteCallback() {
                            @Override
                            public void done(com.parse.ParseException e) {
                                // If removing favorite finished successfully
                                if(e==null) {
                                    btn.setText(btn.getContext().getString(R.string.favorite_inactive));
                                    btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
                                }
                            }
                        });
                    }
                }
            });

            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {

            }
        }

    }
}