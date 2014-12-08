package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment() {

    }

    public String getQuoteId() {
        return getString("QuoteId").toString();
    }

    public void setQuoteId(String quoteId) {
        put("QuoteId", quoteId);
    }

    public String getUserId() {
        return getString("UserId").toString();
    }

    public void setUserId(String userId) {
        put("UserId", userId);
    }

    public String getText() {
        return getString("Text").toString();
    }

    public void setText(String text) {
        put("Text", text);
    }

    @Override
    public String toString() {
        return "UserId: " + getString("userId") + ", " + getString("text");
    }

}
