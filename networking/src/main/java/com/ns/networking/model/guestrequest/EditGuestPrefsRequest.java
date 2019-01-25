package com.ns.networking.model.guestrequest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditGuestPrefsRequest {

    @SerializedName("token")
    private String token;
    @SerializedName("edit")
    private List<GuestPrefsDataRequest> editGuestPrefsRequestList ;

    public List<GuestPrefsDataRequest> getEditGuestPrefsRequestList() {
        return editGuestPrefsRequestList;
    }

    public void setEditGuestPrefsRequestList(List<GuestPrefsDataRequest> editGuestPrefsRequestList) {
        this.editGuestPrefsRequestList = editGuestPrefsRequestList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
