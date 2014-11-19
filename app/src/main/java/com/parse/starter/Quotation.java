package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Quotation")
public class Quotation extends ParseObject {

    public Quotation() {

    }

    public String getCategory() {
        return getString("Category");
    }

    public void setCategory(String category) {
        put("Category", category);
    }

    public String getQuote() {
        return getString("Quote");
    }

    public void setQuote(String quote) {
        put("Quote", quote);
    }

    @Override
    public String toString() {
        return "Category: " + getString("Category") + ", " + getString("Quote");
    }

}
