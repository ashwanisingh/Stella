package com.ns.networking.model.guestrequest;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingUserRelation {

    @SerializedName("seat_id")
    @Expose
    private Integer seatId;
    @SerializedName("passenger")
    @Expose
    private Integer passenger;

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getPassenger() {
        return passenger;
    }

    public void setPassenger(Integer passenger) {
        this.passenger = passenger;
    }

}