package com.ns.networking.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LockedSeats implements Parcelable {

    /*val flight: Flight?,
    val flight_id: Int?,
    val flight_seat: FlightSeat?,
    val flight_seat_id: Int?,
    val from_city: Int?,
    val id: Int?,
    val journey_date: String?,
    val journey_time: String?,
    val seat_reserved_at: String?,
    val status: Int?,
    val to_city: Int?,
    val user: Int?,
    val datetime_ms : Long? ,
    val expire_within_s : Int?,
    val direction : String?,
    val sun_rise_set : String?,
    val arrival_time : String?*/


    public Flight flight;
    public int flight_id;
    public FlightSeat flight_seat;
    public int flight_seat_id;
    public int from_city;
    public int id;
    public String journey_date;
    public String journey_time;
    public String seat_reserved_at;
    public int status;
    public int to_city;
    public int user;
    public long datetime_ms;
    public int expire_within_s;
    public String direction;
    public String sun_rise_set;
    public String arrival_time;

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(int flight_id) {
        this.flight_id = flight_id;
    }

    public FlightSeat getFlight_seat() {
        return flight_seat;
    }

    public void setFlight_seat(FlightSeat flight_seat) {
        this.flight_seat = flight_seat;
    }

    public int getFlight_seat_id() {
        return flight_seat_id;
    }

    public void setFlight_seat_id(int flight_seat_id) {
        this.flight_seat_id = flight_seat_id;
    }

    public int getFrom_city() {
        return from_city;
    }

    public void setFrom_city(int from_city) {
        this.from_city = from_city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJourney_date() {
        return journey_date;
    }

    public void setJourney_date(String journey_date) {
        this.journey_date = journey_date;
    }

    public String getJourney_time() {
        return journey_time;
    }

    public void setJourney_time(String journey_time) {
        this.journey_time = journey_time;
    }

    public String getSeat_reserved_at() {
        return seat_reserved_at;
    }

    public void setSeat_reserved_at(String seat_reserved_at) {
        this.seat_reserved_at = seat_reserved_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTo_city() {
        return to_city;
    }

    public void setTo_city(int to_city) {
        this.to_city = to_city;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public long getDatetime_ms() {
        return datetime_ms;
    }

    public void setDatetime_ms(long datetime_ms) {
        this.datetime_ms = datetime_ms;
    }

    public int getExpire_within_s() {
        return expire_within_s;
    }

    public void setExpire_within_s(int expire_within_s) {
        this.expire_within_s = expire_within_s;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSun_rise_set() {
        return sun_rise_set;
    }

    public void setSun_rise_set(String sun_rise_set) {
        this.sun_rise_set = sun_rise_set;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.flight, flags);
        dest.writeInt(this.flight_id);
        dest.writeParcelable(this.flight_seat, flags);
        dest.writeInt(this.from_city);
        dest.writeInt(this.id);
        dest.writeString(this.journey_date);
        dest.writeString(this.journey_time);
        dest.writeString(this.seat_reserved_at);
        dest.writeInt(this.status);
        dest.writeInt(this.to_city);
        dest.writeInt(this.user);
        dest.writeLong(this.datetime_ms);
        dest.writeInt(this.expire_within_s);
        dest.writeString(this.direction);
        dest.writeString(this.sun_rise_set);
        dest.writeString(this.arrival_time);
    }

    public LockedSeats() {
    }

    protected LockedSeats(Parcel in) {
        this.flight = in.readParcelable(Flight.class.getClassLoader());
        this.flight_id = in.readInt();
        this.flight_seat = in.readParcelable(FlightSeat.class.getClassLoader());
        this.from_city = in.readInt();
        this.id = in.readInt();
        this.journey_date = in.readString();
        this.journey_time = in.readString();
        this.seat_reserved_at = in.readString();
        this.status = in.readInt();
        this.to_city = in.readInt();
        this.user = in.readInt();
        this.datetime_ms = in.readLong();
        this.expire_within_s = in.readInt();
        this.direction = in.readString();
        this.sun_rise_set = in.readString();
        this.arrival_time = in.readString();
    }

    public static final Parcelable.Creator<LockedSeats> CREATOR = new Parcelable.Creator<LockedSeats>() {
        @Override
        public LockedSeats createFromParcel(Parcel source) {
            return new LockedSeats(source);
        }

        @Override
        public LockedSeats[] newArray(int size) {
            return new LockedSeats[size];
        }
    };
}
