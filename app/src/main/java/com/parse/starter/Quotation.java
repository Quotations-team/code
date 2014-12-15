package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Quotation")
public class Quotation extends ParseObject {

    public Quotation() {

    }

    public String[] getCategories() {
        String categories = getString("Category").toString();
        return categories.split(",");
    }

    public void setCategories(String category) {
        put("Category", category);
    }

    public String getQuote() {
        return getString("Quote");
    }

    public void setQuote(String quote) {
        put("Quote", quote);
    }

    public String getAuthor() {
        return getString("Author");
    }

    public void setAuthor(String author) {
        put("Author", author);
    }

    public int getLikes() {
        return getInt("Likes");
    }

    public void addLikes() {
        put("Likes", getLikes() + 1);
    }

    public int getComments() {
        return getInt("Comments");
    }

    public void addComments() {
        put("Comments", getInt("Comments") + 1);
    }

    @Override
    public String toString() {
        return "Category: " + getString("Category") + ", " + getString("Quote");
    }

}
