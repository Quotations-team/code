package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment() {

    }

    public String getQuoteId() {
        return getString("quoteId").toString();
    }

    public void setQuoteId(String quoteId) {
        put("quoteId", quoteId);
    }

    public String getUserName() {
        return getString("userName").toString();
    }

    public void setUserName(String userId) {
        put("userName", userId);
    }

    public String getText() {
        return getString("text").toString();
    }

    public void setText(String text) {
        put("text", text);
    }

    @Override
    public String toString() {
        return "UserName: " + getString("userName") + ", " + getString("text");
    }

}
