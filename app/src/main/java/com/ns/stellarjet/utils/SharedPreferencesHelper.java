package com.ns.stellarjet.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
        mEditor.apply();
    }



}
