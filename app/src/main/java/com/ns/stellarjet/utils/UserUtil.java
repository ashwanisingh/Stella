package com.ns.stellarjet.utils;

import com.ns.networking.model.LockedSeats;
import com.ns.networking.model.UserData;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    public static List<Integer> getFligtSeatsId(UserData userData) {
        List<Integer> seatNamesId = new ArrayList();

        for(LockedSeats locked_seats : userData.getLocked_seats()) {
            if(locked_seats != null) {
                seatNamesId.add(locked_seats.getFlight_seat_id());
            }
        }

        return seatNamesId;
    }
}
