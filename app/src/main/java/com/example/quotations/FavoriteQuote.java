package com.example.quotations;

import android.os.AsyncTask;
import android.widget.Button;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.Favorite;

import java.util.List;


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