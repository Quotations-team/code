package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Category")
public class Category extends ParseObject
{
    public Category()
    {
    	
    }
    
	public String getCategory()
	{
		return getString("Category");
	}
	
	public void setCategory(String category)
	{
		put("Category", category);
	}

	
	@Override
	public String toString()
	{
		return "Quote # ";
	}

}