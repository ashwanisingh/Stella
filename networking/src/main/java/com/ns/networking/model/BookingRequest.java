package com.ns.networking.model;

import com.google.gson.annotations.SerializedName;
import com.ns.networking.model.guestrequest.BookingUserRelation;

import java.util.List;

public class BookingRequest {

    @SerializedName("token")
    private String token;
    @SerializedName("from_city")
    private int fromCity;
    @SerializedName("to_city")
    private int toCity;
    @SerializedName("journey_date")
    private String journeyDate;
    @SerializedName("journey_time")
    private String journeyTime;
    @SerializedName("arrival_time")
    private String arrivalTime;
    @SerializedName("flight_id")
    private int flight_id;
    @SerializedName("travelling_self")
    private int travellingSelf;
    @SerializedName("schedule_id")
    private String schedule_id;
    @SerializedName("guests[]")
    private List<Integer> mGuestList ;
    @SerializedName("seat_ids[]")
    private List<Integer> mSeatIdList ;
    @SerializedName("seat_passenger_relation")
    private List<BookingUserRelation> bookingUserRelation ;

    public BookingRequest(String token, int fromCity, int toCity, String journeyDate, String journeyTime,
                          String arrivalTime, int flight_id, int travellingSelf, String schedule_id,
                          List<Integer> mGuestList, List<Integer> mSeatIdList,
                          List<BookingUserRelation> bookingUserRelation) {
        this.token = token;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.journeyDate = journeyDate;
        this.journeyTime = journeyTime;
        this.arrivalTime = arrivalTime;
        this.flight_id = flight_id;
        this.travellingSelf = travellingSelf;
        this.schedule_id = schedule_id;
        this.mGuestList = mGuestList;
        this.mSeatIdList = mSeatIdList;
        this.bookingUserRelation = bookingUserRelation;
    }

    public String getToken() {
        return token;
    }

    public int getFromCity() {
        return fromCity;
    }

    public int getToCity() {
        return toCity;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public String getJourneyTime() {
        return journeyTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getFlight_id() {
        return flight_id;
    }

    public int getTravellingSelf() {
        return travellingSelf;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public List<Integer> getmGuestList() {
        return mGuestList;
    }

    public List<Integer> getmSeatIdList() {
        return mSeatIdList;
    }

    public List<BookingUserRelation> getBookingUserRelation() {
        return bookingUserRelation;
    }
}
