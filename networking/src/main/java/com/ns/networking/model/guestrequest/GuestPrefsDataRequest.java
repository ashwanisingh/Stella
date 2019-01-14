package com.ns.networking.model.guestrequest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuestPrefsDataRequest {


    @SerializedName("guest_id")
    private Integer guestId;
    @SerializedName("phone")
    private String phone = null;
    @SerializedName("name")
    private String name = null;
    @SerializedName("food_prefs")
    private List<Integer> mFoodPrefsList ;

    public Integer getGuestId() {
        return guestId;
    }

    public void setGuestId(Integer guestId) {
        this.guestId = guestId;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

