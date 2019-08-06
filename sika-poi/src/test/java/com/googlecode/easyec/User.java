package com.googlecode.easyec;

import com.googlecode.easyec.sika.mappings.annotations.ColumnMapping;
import com.googlecode.easyec.sika.mappings.annotations.ColumnReadMapping;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-6
 * Time: 下午1:17
 * To change this template use File | Settings | File Templates.
 */
public class User {

    private String name;
    private String gender;
    private int age;

    @ColumnMapping(column = "A")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ColumnMapping(column = "C")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @ColumnReadMapping(column = "B")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
