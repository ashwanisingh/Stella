package com.ns.stellarjet.utils;

public class UIConstants {

    /**
     * String constant for SharedPreferences Name
     */
    public static final String PREFERENCES_NAME = "StellarPreferences";

    /**
     * String constant for SharedPreferences FingerPrint sensor availability
     */
    public static final String PREFERENCES_FINERPRINT_SENSOR = "FingerPrintSensor";

    /**
     * String constant for SharedPreferences for login status
     */
    public static final String PREFERENCES_IS_LOGGED_IN = "IsLoggedIn";

    /**
     * String constant for SharedPreferences for User Token
     */
    public static final String PREFERENCES_USER_TOKEN = "UserToken";

    /**
     * String constant for SharedPreferences for User refresh Token
     */
    public static final String PREFERENCES_USER_REFRESH_TOKEN = "UserRefreshToken";

    /**
     * String constant for SharedPreferences for Passcode
     */
    public static final String PREFERENCES_PASSCODE = "PassCode";

    /**
     * String constant for SharedPreferences for User ID
     */
    public static final String PREFERENCES_USER_ID = "UserID";
    /**
     * String constant for SharedPreferences for User Name
     */
    public static final String PREFERENCES_USER_NAME = "UserName";
    /**
     * String constant for SharedPreferences for User Email
     */
    public static final String PREFERENCES_USER_EMAIL = "UserEmail";
    /**
     * String constant for SharedPreferences for User Name
     */
    public static final String PREFERENCES_USER_PHONE = "UserPhone";
    /**
     * String constant for bundle name for UserData From API
     */
    public static final String BUNDLE_USER_DATA = "UserData";
    /**
     * String constant for bundle name for UserData From API
     */
    public static final String BUNDLE_SECONDARY_USER_DATA = "SecondaryUserData";
    /**
     * String constant for bundle name for UserType(ie Primary/Secondary) From API
     */
    public static final String PREFERENCES_USER_TYPE = "UserType";
    /**
     * String constant for bundle name for UserData From API
     */
    public static final String TOKEN_NOT_AVAILABLE = "Your session has been expired. Need to login.";

    /**
     * String constant for bundle name for UserData From API
     */
    public static final String USER_TOKEN_EXPIRY = "Provided token is expired.";
    /**
     * String constant for bundle name for booking Id
     */
    public static final String PREFERENCES_BOOKING_ID = "BookingId";
    /**
     * String constant for bundle name for foodType
     */
    public static final String BUNDLE_FOOD_TYPE = "FoodType";
    /**
     * String constant for bundle name for cabType
     */
    public static final String BUNDLE_CAB_TYPE = "CabType";
    /**
     * String constant for bundle name for cabType
     */
    public static final String BUNDLE_CAB_LATLONG = "LatLong";
    /**
     * String constant for bundle value for cabType pickup
     */
    public static final String BUNDLE_CAB_TYPE_PICK = "PickUp";
    /**
     * String constant for bundle value for cabType drop
     */
    public static final String BUNDLE_CAB_TYPE_DROP = "Drop";
    /**
     * String constant for SharedPreferences for personalize Cab
     */
    public static final String PREFERENCES_CAB_PERSONALIZE = "CabPreferences";
    /**
     * String constant for SharedPreferences for personalize pickup Cab
     */
    public static final String PREFERENCES_CAB_PICKUP_PERSONALIZE = "CabPreferencesPickup";
    /**
     * String constant for SharedPreferences for personalize drop Cab
     */
    public static final String PREFERENCES_CAB_DROP_PERSONALIZE = "CabPreferencesDrop";
    /**
     * String constant for SharedPreferences for personalize pickup Cab
     */
    public static final String PREFERENCES_CAB_PICKUP_PERSONALIZE_ID = "CabPreferencesPickupID";
    /**
     * String constant for SharedPreferences for personalize drop Cab
     */
    public static final String PREFERENCES_CAB_DROP_PERSONALIZE_ID = "CabPreferencesDropID";
    /**
     * String constant for SharedPreferences for personalize food
     */
    public static final String PREFERENCES_FOOD_PERSONALIZE = "FoodPreferences";
    /**
     * String constant for SharedPreferences for personalize time limit
     */
    public static final String PREFERENCES_PERSONALIZE_TIME = "PersonalizationTime";
    /**
     * String constant for bundle value for from city
     */
    public static final String BUNDLE_FROM_CITY = "FromCity";
    /**
     * String constant for bundle value for to city
     */
    public static final String BUNDLE_TO_CITY = "ToCity";
    /**
     * String constant for bundle value for to city
     */
    public static final String BUNDLE_SELECTED_CITY_ID = "selectedCityId";
    /**
     * String constant for bundle value for to city
     */
    public static final String BUNDLE_SELECTED_CITY = "selectedCity";

    /*  storing booking data */
    /**
     * String constant for SharedPreferences for from city
     */
    public static final String PREFERENCES_BOOKING_FROM_CITY_ID = "fromCityId";
    /**
     * String constant for SharedPreferences for from city
     */
    public static final String PREFERENCES_BOOKING_FROM_CITY = "fromCity";
    /**
     * String constant for SharedPreferences for to city
     */
    public static final String PREFERENCES_BOOKING_TO_CITY_ID = "toCityId";
    /**
     * String constant for SharedPreferences for to city
     */
    public static final String PREFERENCES_BOOKING_TO_CITY = "toCity";
    /**
     * String constant for SharedPreferences journey date
     */
    public static final String PREFERENCES_BOOKING_JOURNEY_DATE = "journeyDate";
    /**
     * String constant for SharedPreferences journey time
     */
    public static final String PREFERENCES_BOOKING_JOURNEY_TIME = "journeyTime";
    /**
     * String constant for SharedPreferences journey time
     */
    public static final String PREFERENCES_BOOKING_SCHEDULE_ID = "scheduleId";
    /**
     * String constant for SharedPreferences journey timeinmillis
     */
    public static final String PREFERENCES_BOOKING_JOURNEY_TIMEINMILLIS = "journeyTimeOInMillis";
    /**
     * String constant for SharedPreferences journey Arrival time
     */
    public static final String PREFERENCES_BOOKING_JOURNEY_ARRIVAL_TIME = "journeyArrivalTime";
    /**
     * String constant for SharedPreferences flight id
     */
    public static final String PREFERENCES_BOOKING_FLIGHT_ID = "FlightId";
    /**
     * String constant for SharedPreferences seats id
     */
    public static final String PREFERENCES_BOOKING_SEATSID = "SeatsId";
    /**
     * String constant for SharedPreferences seatnames id
     */
    public static final String PREFERENCES_BOOKING_SEATSNAMES = "SeatsNames";
    /**
     * String constant for SharedPreferences device token
     */
    public static final String PREFERENCES_DEVICE_TOKEN = "DeviceToken";
    /**
     * String constant for bundle value for to city
     */
    public static final String BUNDLE_IS_ONLY_GUEST_TRAVELLING= "isOnlyGuestTravelling";
    /**
     * String constant for SharedPreferences current primary user ID
     */
    public static final String PREFERENCES_CURRENT_PRIMARY_USER_ID = "currentPrimaryUserid";
    /**
     * String constant for SharedPreferences current primary user name
     */
    public static final String PREFERENCES_CURRENT_PRIMARY_USER_NAME = "currentPrimaryUserName";
    /**
     * String constant for SharedPreferences primary user selected status
     */
    public static final String PREFERENCES_IS_PRIMARY_USER_SELECTED = "isPrimaryUserSelected";
    /**
     * String constant for SharedPreferences subscription seats count
     */
    public static final String PREFERENCES_SEAT_COUNT    = "seatCount";
    /**
     * String constant for SharedPreferences membership user type(pay_as_u_go/subscription)
     */
    public static final String PREFERENCES_USER_MEMBERSHIP_TYPE = "membershipUserType";

    /**
     * String constant for SharedPreferences membership user type - pay_as_u_go
     */
    public static final String PREFERENCES_MEMBERSHIP_PAY_AS_U_GO= "PayAsYouGo";
    /**
     * String constant for SharedPreferences membership user type - subscription
     */
    public static final String PREFERENCES_MEMBERSHIP_SUBSCRIPTION= "Subscription";
    /**
     * String constant for SharedPreferences individual seat cost
     */
    public static final String PREFERENCES_SEAT_COST = "seatCost";
    /**
     * String constant for SharedPreferences individual seat cost
     */
    public static final String PREFERENCES_FIRST_TIME_UI = "EntryUI";
}
