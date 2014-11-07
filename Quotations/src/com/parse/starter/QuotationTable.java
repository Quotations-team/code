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
	
	public String getCategory()
	{
		return getString("Category");
	}
	
	public void setCategory(String category)
	{
		put("Category", category);
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
		return "Quote # " + getID() + "\nCategory: " + getCategory() + "\nQuote: " + getQuote();
	}
}
