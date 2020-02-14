package com.example.in_help.category;

public class CategoryData {
    private String categoryName;
    private Integer numberOfServices;

    public CategoryData() {
    }

    public CategoryData(final String categoryName, final Integer numberOfServices) {
        this.categoryName = categoryName;
        this.numberOfServices = numberOfServices;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getNumberOfServices() {
        return numberOfServices;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setNumberOfServices(Integer numberOfServices) {
        this.numberOfServices = numberOfServices;
    }
}
