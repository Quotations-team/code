package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {

    public Like() {

    }

    public Object getQuote() {
        return get("quote");
    }

    public void setQuote(Object quote) {
        put("quote", quote);
    }

    public Object getUser() {
        return get("user");
    }

    public void setUser(Object user) {
        put("user", user);
    }

    @Override
    public String toString() {
        return "User: " + ((ParseUser)get("user")).getUsername() + ", Quote:" + ((Quotation)get("quote")).toString();
    }

}
