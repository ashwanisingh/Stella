package com.ns.networking.model;

public class AddGuestRequestData {

    private String guestId;
    private String guestName;
    private String guestMobileNUmber;
    private int guestFoodPreferences;
    private String guestStatus;

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public void setGuestMobileNUmber(String guestMobileNUmber) {
        this.guestMobileNUmber = guestMobileNUmber;
    }

    public void setGuestFoodPreferences(int guestFoodPreferences) {
        this.guestFoodPreferences = guestFoodPreferences;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestMobileNUmber() {
        return guestMobileNUmber;
    }

    public int getGuestFoodPreferences() {
        return guestFoodPreferences;
    }

    public String getGuestStatus() {
        return guestStatus;
    }

    public void setGuestStatus(String guestStatus) {
        this.guestStatus = guestStatus;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }
}
