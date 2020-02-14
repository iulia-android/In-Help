package com.example.in_help.service;

public class ServiceItem {

    private String name;
    private float price;
    private float rating;
    private String username;
    private String description;
    private String city;
    private String category;
    private boolean periodic;
    private String days;
    private String time;
    private float number_of_ratings;
    private String customer;
    private String timestamp;
    private String precise_location;

    public ServiceItem() {
    }

    public ServiceItem(final String name, final float price, final float rating, final String username,
                       final String description, final String city, final boolean periodic,
                       final String category, final String days, final String time,
                       final float number_of_ratings, final String customer, final String timestamp, final String precise_location) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.username = username;
        this.description = description;
        this.city = city;
        this.periodic = periodic;
        this.category = category;
        this.days = days;
        this.number_of_ratings = number_of_ratings;
        this.customer = customer;
        this.timestamp = timestamp;
        this.precise_location = precise_location;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getNumber_of_ratings() {
        return number_of_ratings;
    }

    public void setNumber_of_ratings(float number_of_ratings) {
        this.number_of_ratings = number_of_ratings;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public void setPeriodic(boolean periodic) {
        this.periodic = periodic;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrecise_location() {
        return precise_location;
    }

    public void setPrecise_location(String precise_location) {
        this.precise_location = precise_location;
    }
}
