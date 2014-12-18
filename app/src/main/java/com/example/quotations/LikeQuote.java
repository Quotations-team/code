package com.example.quotations;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.Like;
import com.parse.starter.Quotation;

import java.util.List;


class LikeQuote extends AsyncTask<Object, Boolean, Boolean> {

    Button btn;
    ParseUser currentUser;
    Quotation currentQuote;
    ButtonStatus currentButtonStatus;

    protected void onPreExecute()
    {

    }

    protected Boolean doInBackground(Object... params) {
        try
        {
            currentUser = (ParseUser) params[0];
            btn = (Button) params[1];

            currentQuote = (Quotation)btn.getTag();

            ParseQuery<Like> likeQuery = ParseQuery.getQuery("Like");
            likeQuery.whereEqualTo("user", currentUser);
            likeQuery.whereEqualTo("quote", currentQuote);
            List<Like> data = likeQuery.find();

            if (data.size() == 0)
            {
                // Retrieve the quote by id
                ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
                currentQuote = query.get(currentQuote.getObjectId());
                if (currentQuote != null)
                {
                    // add one number to the quote total likes counter
                    currentQuote.addLikes();
                    currentQuote.save();

                    // Create like object and save it in cloud
                    Like like = new Like();
                    like.put("user", currentUser);
                    like.put("quote", currentQuote);
                    like.saveInBackground();

                    QuotationsApplication.likes.add(like);
                    // Saving like finished successfully
                    currentButtonStatus = ButtonStatus.active;

                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    protected void onPostExecute(Boolean result) {
        try {
        if (result)
            setLikeButtonLayout(btn, currentButtonStatus);

        // get quote from button parent view then get the status text
            TextView currentStatusTextView = (TextView) ((View) btn.getParent().getParent()).findViewById(R.id.quoteStatus);
            currentStatusTextView.setText(QuotationsHelper.getStatusText(currentQuote));
        }
        catch (Exception ex) {
        }
    }

    public static void setLikeButtonLayout(Button likeButton, ButtonStatus buttonStatus) {
        if (buttonStatus.equals(ButtonStatus.active)) {
            likeButton.setText(likeButton.getContext().getString(R.string.like_active));
            likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_active, 0, 0, 0);
        }
        else if(buttonStatus.equals(ButtonStatus.inactive))
        {
            likeButton.setText(likeButton.getContext().getString(R.string.like_inactive));
            likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like, 0, 0, 0);
        }
    }

    public static View.OnClickListener onLikeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Button likeButton = (Button)v;
                if(likeButton.getText().equals(v.getContext().getString(R.string.like_inactive)))
                {
                    new LikeQuote().execute(currentUser, v);
                    //LikeQuote.setLikeButtonLayout(likeButton, true);
                }
            } else {
                QuotationsHelper.showAlertDialog(v.getContext(),
                        v.getContext().getString(R.string.ask_for_registration));
            }
        }
    };
}
