package com.googlecode.easyec.sika.csv.test;

import com.googlecode.easyec.sika.mappings.annotations.ColumnReadMapping;

public class Customer {

    private String firstName;
    private String lastName;
    private String gender;

    @ColumnReadMapping(column = "A")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ColumnReadMapping(column = "B")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ColumnReadMapping(column = "C")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
