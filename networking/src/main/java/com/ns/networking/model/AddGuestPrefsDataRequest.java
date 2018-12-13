package com.ns.networking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddGuestPrefsDataRequest {


    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone = null;
    @SerializedName("food_prefs")
    private List<Integer> mFoodPrefsList ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Integer> getmFoodPrefsList() {
        return mFoodPrefsList;
    }

    public void setmFoodPrefsList(List<Integer> mFoodPrefsList) {
        this.mFoodPrefsList = mFoodPrefsList;
    }


}

