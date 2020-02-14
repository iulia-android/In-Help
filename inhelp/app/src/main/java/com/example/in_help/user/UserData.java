package com.example.in_help.user;

public class UserData {
    private String name;
    private Integer age;
    private String country;
    private String town;
    private String occupation;
    private String description;
    private String preferences;
    private String offerServices;

    public UserData() {
    }

    public UserData(final String fullName, final Integer age, final String country,
                    final String town, final String occupation, final String description,
                    final String preferences, final String offerServices) {
        this.name = fullName;
        this.age = age;
        this.country = country;
        this.town = town;
        this.occupation = occupation;
        this.description = description;
        this.preferences = preferences;
        this.offerServices = offerServices;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public String getTown() {
        return town;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getDescription() {
        return description;
    }

    public String getPreferences() {
        return preferences;
    }

    public String getOfferServices() {
        return offerServices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public void setOfferServices(String offerServices) {
        this.offerServices = offerServices;
    }
}
