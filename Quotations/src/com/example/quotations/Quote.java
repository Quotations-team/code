package com.example.quotations;

/**
 * Created by Saman on 11/13/2014.
 */
public class Quote {
    int QuoteId;
    String QuoteText;
    String CategoryName;

    public Quote(int QuoteId, String QuoteText, String CategoryName)
    {
        this.QuoteText = QuoteText;
        this.QuoteId = QuoteId;
        this.CategoryName = CategoryName;
    }


}
