package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

//this subclass of ParseObject need to register in the Application, see in ParseApplication.
//@ParseClassName("QuotationTable") is tell SDK this class object refer to QuotationTable table in the parse.com server
@ParseClassName("QuotationTable")
public class QuotationTable extends ParseObject
{
	//default constructor
	public QuotationTable()
	{
		
	}
	
	//retrieving data
	public int getID()
	{
		//getInt("ID") is ParseObject method, ID is our field name
		return getInt("ID");
	}
	
	//saving data
	public void setID(int id)
	{
		//put("ID", id) is ParseObject method, ID is our field name, id the input data
		put("ID", id);
	}
	
	public String getTopic()
	{
		return getString("Topic");
	}
	
	public void setTopic(String topic)
	{
		put("Topic", topic);
	}
	
	public String getQuote()
	{
		return getString("Quote");
	}
	
	public void setQuote(String quote)
	{
		put("Quote", quote);
	}
	
	//just a way we want to display this object
	@Override
	public String toString()
	{
		return "Quote # " + getID() + "\nType: " + getTopic() + "\nQuote: " + getQuote();
	}
}
