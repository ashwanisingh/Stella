package com.ns.networking.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DropAddressBean implements Parcelable {
    /**
     * id : 13
     * address : neben 67/1, HSR Layout, KG Halli, D' Souza Layout, Ashok Nagar, Bengaluru, Karnataka 560002, India
     * address_tag : usissisi
     * lat : null
     * lng : null
     */


    private int id;
    private String address;
    private String address_tag;
    private String lat;
    private String lng;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_tag() {
        return address_tag;
    }

    public void setAddress_tag(String address_tag) {
        this.address_tag = address_tag;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.address);
        dest.writeString(this.address_tag);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
    }

    public DropAddressBean() {
    }

    protected DropAddressBean(Parcel in) {
        this.id = in.readInt();
        this.address = in.readString();
        this.address_tag = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
    }

    public static final Parcelable.Creator<DropAddressBean> CREATOR = new Parcelable.Creator<DropAddressBean>() {
        @Override
        public DropAddressBean createFromParcel(Parcel source) {
            return new DropAddressBean(source);
        }

        @Override
        public DropAddressBean[] newArray(int size) {
            return new DropAddressBean[size];
        }
    };
}
