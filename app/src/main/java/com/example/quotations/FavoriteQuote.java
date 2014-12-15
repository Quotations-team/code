package com.example.quotations;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Favorite;
import com.parse.starter.Quotation;

import java.util.List;


class FavoriteQuote extends AsyncTask<Object, Boolean, Boolean> {

    protected void onPreExecute() {

    }

    Button btn;
    ParseUser currentUser;
    ButtonStatus currentButtonStatus;

    protected Boolean doInBackground(Object... params)
    {
        try
        {
            currentUser = (ParseUser) params[0];
            btn = (Button) params[1];

            Quotation quote = (Quotation)btn.getTag();

            ParseQuery<Favorite> favoriteQuery = ParseQuery.getQuery("Favorite");
            favoriteQuery.whereEqualTo("user", currentUser);
            favoriteQuery.whereEqualTo("quote", quote);
            List<Favorite> data = favoriteQuery.find();

            // If favorite does not exists
            if (data.size() == 0) {
                // Create like object and save it in cloud
                Favorite favorite = new Favorite();
                favorite.put("user", currentUser);
                favorite.put("quote", quote);
                favorite.save();

                QuotationsApplication.favorites.add(favorite);
                // Saving favorite finished successfully
                currentButtonStatus = ButtonStatus.active;
            }
            else
            {
                // quote was added in favourite before
                // delete it from database
                QuotationsApplication.favorites.remove(data.get(0));
                data.get(0).delete();

                // If removing favorite finished successfully
                currentButtonStatus = ButtonStatus.inactive;
            }
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    protected void onPostExecute(Boolean result) {
        if(result)
            setFavoriteButtonLayout(btn, currentButtonStatus);
    }

    public static void setFavoriteButtonLayout(Button favoriteButton, ButtonStatus buttonStatus)
    {
        if (buttonStatus.equals(ButtonStatus.active)) {
            favoriteButton.setText(favoriteButton.getContext().getString(R.string.favorite_active));
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_active, 0, 0, 0);
        }
        else if(buttonStatus.equals(ButtonStatus.inactive))
        {
            favoriteButton.setText(favoriteButton.getContext().getString(R.string.favorite_inactive));
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
        }
    }

    public static View.OnClickListener onFavoriteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null)
                new FavoriteQuote().execute(currentUser, v);
            else
                QuotationsHelper.showAlertDialog(v.getContext(), v.getContext().getString(R.string.ask_for_registration));
        }
    };

}