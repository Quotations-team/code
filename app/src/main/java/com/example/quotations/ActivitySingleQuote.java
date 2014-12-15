package com.example.quotations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.Comment;
import com.parse.starter.Favorite;
import com.parse.starter.Like;
import com.parse.starter.Quotation;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/*
 * this Activity is called by splash screen.
 */
public class ActivitySingleQuote extends Activity {

    private ParseUser currentUser;
    Quotation quote;

    private List<Comment> comments;
    private ListView commentsListView;
    private ListViewCommentAdapter adapter;
    Button likeButton;
    Button favoriteButton;
    Button commentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String quoteId = intent.getStringExtra("quoteId");
        final String quoteText = intent.getStringExtra("quoteText");
        final String quoteAuthor = intent.getStringExtra("quoteAuthor");
        String quoteStatus = intent.getStringExtra("quoteStatus");

        if(quoteId != null && quoteText != null && quoteAuthor != null && quoteStatus != null)
        {
            setContentView(R.layout.layout_single_quote);

            currentUser = ParseUser.getCurrentUser();
            comments = new ArrayList<Comment>();

            // Set Quote Contents in controls
            TextView quoteTextView = (TextView)findViewById(R.id.quoteText);
            TextView authorTextView = (TextView)findViewById(R.id.quoteAuthor);
            final TextView statusTextView = (TextView)findViewById(R.id.quoteStatus);
            favoriteButton = (Button)findViewById(R.id.addToFavorite);
            likeButton = (Button)findViewById(R.id.like);
            commentButton = (Button)findViewById(R.id.comment);

            quoteTextView.setText("“" + quoteText + "”");
            quoteTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/sanchez.ttf"));
            authorTextView.setText(quoteAuthor);
            statusTextView.setText(quoteStatus);


            // Init Comment ListView
            commentsListView = (ListView) findViewById(R.id.listViewComments);
            commentsListView.setEmptyView(findViewById(R.id.commentEmptyListLayout));
            commentsListView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    //loadQuotes(getCategoryFilter(), searchTerm, page);
                }
            });
            loadComments(quoteId);

            final EditText commentEditText = (EditText)findViewById(R.id.editTextComment);
            commentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        Comment comment = new Comment();
                        comment.setQuoteId(quoteId);
                        comment.setUserName(currentUser.getUsername());
                        String text = commentEditText.getText().toString();
                        comment.setText(text);

                        comment.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(com.parse.ParseException e) {
                                if (e == null) {
                                    loadComments(quoteId);
                                    commentEditText.setText("");
                                    commentEditText.clearFocus();

                                    // code to hide the soft keyboard
                                    InputMethodManager imm = (InputMethodManager) getSystemService(
                                            commentEditText.getContext().INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(commentEditText.getApplicationWindowToken(), 0);

                                    ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
                                    // Retrieve the object by id
                                    query.getInBackground(quoteId, new GetCallback<Quotation>() {
                                        public void done(final Quotation quote, com.parse.ParseException e) {
                                            if (e == null) {
                                                quote.addComments();
                                                quote.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        // Update Status
                                                        statusTextView.setText(QuotationsHelper.getStatusText(quote));
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
                    return false;
                }
            });

            // Get quote by quoteId
            ParseQuery<Quotation> query = ParseQuery.getQuery("Quotation");
            query.getInBackground(quoteId, new GetCallback<Quotation>() {
                @Override
                public void done(Quotation quotation, ParseException e) {
                    if(e == null)
                    {
                        // Set quote on button tag to use it when button clicked
                        quote = quotation;
                        likeButton.setTag(quote);
                        favoriteButton.setTag(quote);

                        // Set button events
                        likeButton.setOnClickListener(LikeQuote.onLikeClick);
                        favoriteButton.setOnClickListener(FavoriteQuote.onFavoriteClick);
                        commentButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText ed = (EditText)((View)v.getParent().getParent()).findViewById(R.id.editTextComment);
                                ed.requestFocus();
                            }
                        });

                        // Get favorite status
                        ParseQuery<Favorite> queryFavorite = ParseQuery.getQuery("Favorite");
                        queryFavorite.whereEqualTo("user", currentUser);
                        queryFavorite.whereEqualTo("quote", quote);
                        queryFavorite.findInBackground(new FindCallback<Favorite>() {
                            @Override
                            public void done(List<Favorite> data, ParseException e) {
                                if (e == null) {
                                    if (data.size() > 0)
                                        FavoriteQuote.setFavoriteButtonLayout(favoriteButton, ButtonStatus.active);
                                }
                            }
                        });

                        // Get favorite status
                        ParseQuery<Like> queryLike = ParseQuery.getQuery("Like");
                        queryLike.whereEqualTo("user", currentUser);
                        queryLike.whereEqualTo("quote", quote);
                        queryLike.findInBackground(new FindCallback<Like>() {
                            @Override
                            public void done(List<Like> data, ParseException e) {
                                if (e == null) {
                                    if (data.size() > 0)
                                        LikeQuote.setLikeButtonLayout(likeButton, ButtonStatus.active);
                                }
                            }
                        });
                    }
                }
            });
        }
        else
        {
            finish();
        }
    }

    private void loadComments(String quoteId)
    {
        // Get Comments
        final ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo("quoteId", quoteId);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> data, ParseException e) {
                if (e == null) {
                    comments = data;
                    updateCommentListView();
                }
            }
        });
    }

    private void updateCommentListView() {
        adapter = new ListViewCommentAdapter(this, comments);
        commentsListView.setAdapter(adapter);
    }

}