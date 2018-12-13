package com.ns.networking.model.guestrequest;

import android.widget.Button;

public class BookedSeatsRequest {

    private int seatId;
    private Button mDesiredButton;
    private String seatPosition;

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public Button getmDesiredButton() {
        return mDesiredButton;
    }

    public void setmDesiredButton(Button mDesiredButton) {
        this.mDesiredButton = mDesiredButton;
    }

    public String getSeatPosition() {
        return seatPosition;
    }

    public void setSeatPosition(String seatPosition) {
        this.seatPosition = seatPosition;
    }
}
