package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Category")
public class Category extends ParseObject {
    public Category() {

    }

    public String getCategory() {
        return getString("CategoryName");
    }

    public void setCategory(String category) {
        put("CategoryName", category);
    }


    @Override
    public String toString() {
        return getString("CategoryName");
    }

}