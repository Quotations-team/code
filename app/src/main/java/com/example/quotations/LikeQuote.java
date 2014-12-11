package com.example.quotations;

import android.os.AsyncTask;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.Like;
import com.parse.starter.Quotation;

import java.util.List;


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
