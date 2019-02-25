package com.ns.stellarjet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ns.stellarjet.booking.PassengerListActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesHelper {


    private static SharedPreferences getSharedPreferences(Context mContext){
        return mContext.getSharedPreferences(
                UIConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void saveFingerPrintHardwareStatus(Context mContext , boolean isPresent){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putBoolean(UIConstants.PREFERENCES_FINERPRINT_SENSOR , isPresent);
        mEditor.apply();
    }

    public static boolean isFingerPrintAvailable(Context mContext){
        return getSharedPreferences(mContext).getBoolean(
                UIConstants.PREFERENCES_FINERPRINT_SENSOR , false
        );
    }

    public static void saveLoginStatus(Context mContext , boolean isPresent){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putBoolean(UIConstants.PREFERENCES_IS_LOGGED_IN, isPresent);
        mEditor.apply();
    }

    public static boolean isUserLoggedIn(Context mContext){
        return getSharedPreferences(mContext).getBoolean(
                UIConstants.PREFERENCES_IS_LOGGED_IN , false
        );
    }

    public static void saveUserToken(Context mContext , String token){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_TOKEN, token);
        mEditor.apply();
    }

    public static String getUserToken(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_USER_TOKEN , ""
        );
    }

    public static void saveUserRefreshToken(Context mContext , String token){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_REFRESH_TOKEN, token);
        mEditor.apply();
    }

    public static String getUserRefreshToken(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_USER_REFRESH_TOKEN , ""
        );
    }

    public static void savePassCode(Context mContext , String passcode){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_PASSCODE, passcode);
        mEditor.apply();
    }

    public static String getPassCode(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_PASSCODE , ""
        );
    }

    public static void saveUserId(Context mContext , String passcode){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_ID, passcode);
        mEditor.apply();
    }

    public static String getUserId(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_USER_ID , ""
        );
    }


    public static void saveUserName(Context mContext , String name){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_NAME, name);
        mEditor.apply();
    }

    public static String getUserName(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_USER_NAME , ""
        );
    }

    public static void saveUserEmail(Context mContext , String email){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_EMAIL, email);
        mEditor.apply();
    }

    public static String getUserEmail(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_USER_EMAIL , ""
        );
    }

    public static void saveUserPhone(Context mContext , String phone){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_PHONE, phone);
        mEditor.apply();
    }

    public static String getUserPhone(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_USER_PHONE , ""
        );
    }

    public static void saveUserType(Context mContext , String type){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_TYPE, type);
        mEditor.apply();
    }

    public static String getUserType(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_USER_TYPE , ""
        );
    }

    public static void saveBookingId(Context mContext , String bookingId){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_ID, bookingId);
        mEditor.apply();
    }

    public static String getBookingId(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_BOOKING_ID , ""
        );
    }

    public static void saveFoodPersonalize(Context mContext , boolean isPersonalized){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putBoolean(UIConstants.PREFERENCES_FOOD_PERSONALIZE, isPersonalized);
        mEditor.apply();
    }

    public static boolean getFoodPersonalize(Context mContext){
        return getSharedPreferences(mContext).getBoolean(
                UIConstants.PREFERENCES_FOOD_PERSONALIZE , false
        );
    }

    public static void saveCabPersonalize(Context mContext , boolean isPersonalized){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putBoolean(UIConstants.PREFERENCES_CAB_PERSONALIZE, isPersonalized);
        mEditor.apply();
    }

    public static boolean getCabPersonalize(Context mContext){
        return getSharedPreferences(mContext).getBoolean(
                UIConstants.PREFERENCES_CAB_PERSONALIZE , false
        );
    }

    public static void saveCabPickupPersoalize(Context mContext , String pickUoAddress){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_CAB_PICKUP_PERSONALIZE, pickUoAddress);
        mEditor.apply();
    }

    public static String getCabPickupPersonlalize(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_CAB_PICKUP_PERSONALIZE , ""
        );
    }

    public static void saveCabPickupPersoalizeID(Context mContext , String pickUoAddressID){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_CAB_PICKUP_PERSONALIZE_ID, pickUoAddressID);
        mEditor.apply();
    }

    public static String getCabPickupPersonlalizeID(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_CAB_PICKUP_PERSONALIZE_ID , ""
        );
    }

    public static void saveCabDropPersonalize(Context mContext , String pickUoAddress){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_CAB_DROP_PERSONALIZE, pickUoAddress);
        mEditor.apply();
    }

    public static String getCabDropPersonalize(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_CAB_DROP_PERSONALIZE , ""
        );
    }

    public static void saveCabDropPersonalizeID(Context mContext , String drooAddressId){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_CAB_DROP_PERSONALIZE_ID, drooAddressId);
        mEditor.apply();
    }

    public static String getCabDropPersonalizeID(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_CAB_DROP_PERSONALIZE_ID , ""
        );
    }

    public static void savePersonalizeTime(Context mContext , String dateTime){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_PERSONALIZE_TIME, dateTime);
        mEditor.apply();
    }

    public static String getPersonalizeTime(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_PERSONALIZE_TIME , ""
        );
    }

    public static void saveFromCityId(Context mContext , int fromCityId){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putInt(UIConstants.PREFERENCES_BOOKING_FROM_CITY_ID, fromCityId);
        mEditor.apply();
    }

    public static int getFromCityId(Context mContext){
        return getSharedPreferences(mContext).getInt(
                UIConstants.PREFERENCES_BOOKING_FROM_CITY_ID , 0
        );
    }

    public static void saveFromCity(Context mContext , String fromCity){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_FROM_CITY, fromCity);
        mEditor.apply();
    }

    public static String getFromCity(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_BOOKING_FROM_CITY , ""
        );
    }

    public static void saveToCityId(Context mContext , int toCityId){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putInt(UIConstants.PREFERENCES_BOOKING_TO_CITY_ID, toCityId);
        mEditor.apply();
    }

    public static int getToCityId(Context mContext){
        return getSharedPreferences(mContext).getInt(
                UIConstants.PREFERENCES_BOOKING_TO_CITY_ID , 0
        );
    }

    public static void saveToCity(Context mContext , String toCity){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_TO_CITY, toCity);
        mEditor.apply();
    }

    public static String getToCity(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_BOOKING_TO_CITY , ""
        );
    }

    public static void saveJourneyDate(Context mContext , String journeyDate){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_JOURNEY_DATE, journeyDate);
        mEditor.apply();
    }

    public static String getJourneyDate(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_BOOKING_JOURNEY_DATE , ""
        );
    }

    public static void saveJourneyTime(Context mContext , String journeyTime){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_JOURNEY_TIME, journeyTime);
        mEditor.apply();
    }

    public static String getJourneyTime(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_BOOKING_JOURNEY_TIME , ""
        );
    }

    public static void saveScheduleId(Context mContext , String scheduleId){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_SCHEDULE_ID, scheduleId);
        mEditor.apply();
    }

    public static String getScheduleId(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_BOOKING_SCHEDULE_ID , ""
        );
    }

    public static void saveJourneyTimeImMillis(Context mContext , long journeyTimeInMIllis){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putLong(UIConstants.PREFERENCES_BOOKING_JOURNEY_TIMEINMILLIS, journeyTimeInMIllis);
        mEditor.apply();
    }

    public static long getJourneyTimeImMillis(Context mContext){
        return getSharedPreferences(mContext).getLong(
                UIConstants.PREFERENCES_BOOKING_JOURNEY_TIMEINMILLIS , 0
        );
    }

    public static void saveArrivalTime(Context mContext , String arrivalTime){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_JOURNEY_ARRIVAL_TIME, arrivalTime);
        mEditor.apply();
    }

    public static String getArrivalTime(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_BOOKING_JOURNEY_ARRIVAL_TIME, ""
        );
    }

    public static void saveFlightId(Context mContext , int flightId){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putInt(UIConstants.PREFERENCES_BOOKING_FLIGHT_ID, flightId);
        mEditor.apply();
    }

    public static int getFlightId(Context mContext){
        return getSharedPreferences(mContext).getInt(
                UIConstants.PREFERENCES_BOOKING_FLIGHT_ID, 0
        );
    }

    public static void saveDeviceToken(Context mContext , String token){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_DEVICE_TOKEN, token);
        mEditor.apply();
    }

    public static String getDeviceToken(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_DEVICE_TOKEN, ""
        );
    }

    public static void saveCurrentPrimaryUserId(Context mContext , int primaryUserID){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putInt(UIConstants.PREFERENCES_CURRENT_PRIMARY_USER_ID, primaryUserID);
        mEditor.apply();
    }

    public static int getCurrentPrimaryUserId(Context mContext){
        return getSharedPreferences(mContext).getInt(
                UIConstants.PREFERENCES_CURRENT_PRIMARY_USER_ID, 0
        );
    }

    public static void saveCurrentPrimaryUserName(Context mContext , String primaryUserName){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_CURRENT_PRIMARY_USER_NAME, primaryUserName);
        mEditor.apply();
    }

    public static String getCurrentPrimaryUserName(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_CURRENT_PRIMARY_USER_NAME, ""
        );
    }

    public static void setPrimaryUserSelectionStatus(Context mContext , boolean isSelected){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putBoolean(UIConstants.PREFERENCES_IS_PRIMARY_USER_SELECTED, isSelected);
        mEditor.apply();
    }

    public static boolean isPrimaryUserSelected(Context mContext){
        return getSharedPreferences(mContext).getBoolean(
                UIConstants.PREFERENCES_IS_PRIMARY_USER_SELECTED, false
        );
    }

    public static void saveSeatCount(Context mContext , Integer seatCount){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putInt(UIConstants.PREFERENCES_SEAT_COUNT, seatCount);
        mEditor.apply();
    }

    public static int getSeatCount(Context mContext){
        return getSharedPreferences(mContext).getInt(
                UIConstants.PREFERENCES_SEAT_COUNT, 0
        );
    }

    public static void saveMembershipType(Context mContext , String membershipType){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_MEMBERSHIP_TYPE, membershipType);
        mEditor.apply();
    }

    public static String getMembershipType(Context mContext){
        return getSharedPreferences(mContext).getString(
                UIConstants.PREFERENCES_USER_MEMBERSHIP_TYPE, ""
        );
    }

    public static void saveSeatCost(Context mContext , Integer seatCost){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putInt(UIConstants.PREFERENCES_SEAT_COST, seatCost);
        mEditor.apply();
    }

    public static int getSeatCost(Context mContext){
        return getSharedPreferences(mContext).getInt(
                UIConstants.PREFERENCES_SEAT_COST, 0
        );
    }



    /**
     * clears all SharedPreferences if passcode attempts are failed
     * @param mContext
     */
    public static void clearAllSharedPreferencesData(Context mContext){
        SharedPreferences.Editor mEditor = getSharedPreferences(mContext).edit();
        mEditor.putString(UIConstants.PREFERENCES_USER_PHONE, "");
        mEditor.putString(UIConstants.PREFERENCES_USER_EMAIL, "");
        mEditor.putString(UIConstants.PREFERENCES_USER_NAME, "");
        mEditor.putString(UIConstants.PREFERENCES_USER_ID, "");
        mEditor.putString(UIConstants.PREFERENCES_USER_TOKEN, "");
        mEditor.putString(UIConstants.PREFERENCES_USER_REFRESH_TOKEN, "");
        mEditor.putBoolean(UIConstants.PREFERENCES_IS_LOGGED_IN, false);
        mEditor.putString(UIConstants.PREFERENCES_PASSCODE, "");
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_FROM_CITY_ID, "");
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_TO_CITY_ID, "");
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_JOURNEY_ARRIVAL_TIME, "");
        mEditor.putLong(UIConstants.PREFERENCES_BOOKING_JOURNEY_TIMEINMILLIS, 0);
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_JOURNEY_TIME,"");
        mEditor.putString(UIConstants.PREFERENCES_BOOKING_JOURNEY_DATE,"");
        mEditor.putInt(UIConstants.PREFERENCES_BOOKING_FLIGHT_ID,0);
        mEditor.putInt(UIConstants.PREFERENCES_BOOKING_FROM_CITY,0);
        mEditor.putInt(UIConstants.PREFERENCES_BOOKING_TO_CITY,0);
        mEditor.apply();
    }

    public static void clearAllBookingData(Context context){
        SharedPreferencesHelper.saveFlightId(context , 0);
        SharedPreferencesHelper.saveArrivalTime(context , "");
        SharedPreferencesHelper.saveFromCityId(context , 0);
        SharedPreferencesHelper.saveToCityId(context , 0);
        SharedPreferencesHelper.saveToCity(context , "");
        SharedPreferencesHelper.saveFromCity(context , "");
        SharedPreferencesHelper.saveJourneyTimeImMillis(context , 0);
        SharedPreferencesHelper.saveJourneyTime(context , "");
        SharedPreferencesHelper.saveJourneyDate(context , "");
        SharedPreferencesHelper.saveArrivalTime(context , "");
        SharedPreferencesHelper.saveScheduleId(context , "");
    }



}
