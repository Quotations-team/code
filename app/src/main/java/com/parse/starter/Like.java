package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Like")
public class Like extends ParseObject {

    public Like() {

    }

    public Object getQuoteId() {
        return get("quoteId");
    }

    public void setQuoteId(Object quoteId) {
        put("quoteId", quoteId);
    }

    public Object getUserId() {
        return get("userId");
    }

    public void setUserId(Object userId) {
        put("userId", userId);
    }

    @Override
    public String toString() {
        return "UserId: " + get("userId") + ", QuoteId:" + get("quoteId");
    }

}
