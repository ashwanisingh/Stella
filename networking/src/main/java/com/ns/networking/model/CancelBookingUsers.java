package com.ns.networking.model;

public class CancelBookingUsers {

    private String seatName;
    private int seatId;
    private String passengerName;
    private boolean isSelected;

    public CancelBookingUsers(String seatName, int seatId, String passengerName, boolean isSelected) {
        this.seatName = seatName;
        this.seatId = seatId;
        this.passengerName = passengerName;
        this.isSelected = isSelected;
    }

    public String getSeatName() {
        return seatName;
    }

    public int getSeatId() {
        return seatId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
