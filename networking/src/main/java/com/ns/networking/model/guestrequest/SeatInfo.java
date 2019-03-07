package com.ns.networking.model.guestrequest;

import android.os.Parcel;
import android.os.Parcelable;

public class SeatInfo implements Parcelable {

    private String seatName;
    private Integer seatId;

    protected SeatInfo(Parcel in) {
        seatName = in.readString();
        if (in.readByte() == 0) {
            seatId = null;
        } else {
            seatId = in.readInt();
        }
    }

    public static final Creator<SeatInfo> CREATOR = new Creator<SeatInfo>() {
        @Override
        public SeatInfo createFromParcel(Parcel in) {
            return new SeatInfo(in);
        }

        @Override
        public SeatInfo[] newArray(int size) {
            return new SeatInfo[size];
        }
    };

    public String getSeatName() {
        return seatName;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public SeatInfo(String seatName, Integer seatId) {
        this.seatName = seatName;
        this.seatId = seatId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(seatName);
        if (seatId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(seatId);
        }
    }
}
