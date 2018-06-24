package com.motion.laundryq_partner.model;

public class CategoryModel {
    private String categoryID;
    private String category_name;
    private int categoryPrice;
    private String categoryUnit;

    public CategoryModel() {
    }

    public CategoryModel(String categoryID, String category_name) {
        this.categoryID = categoryID;
        this.category_name = category_name;
    }

    public CategoryModel(String categoryID, String category_name, int categoryPrice, String categoryUnit) {
        this.categoryID = categoryID;
        this.category_name = category_name;
        this.categoryPrice = categoryPrice;
        this.categoryUnit = categoryUnit;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }

    public int getCategoryPrice() {
        return categoryPrice;
    }

    public void setCategoryPrice(int categoryPrice) {
        this.categoryPrice = categoryPrice;
    }

    public String getCategoryUnit() {
        return categoryUnit;
    }

    public void setCategoryUnit(String categoryUnit) {
        this.categoryUnit = categoryUnit;
    }
}
