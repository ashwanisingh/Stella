package com.ns.stellarjet.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StellarJetUtils {


    /**
     * This method used to get the displayMetrics from the WindowManager
     * @param context to get the application context
     * @return DisplayMetrics - A structure describing general information about a display, such as its
     * size, density, and font scaling.
     */
    private static DisplayMetrics getDisplayMetrics(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * This method used to get the screen width
     * @param context to get the Application Context
     * @return int - width of the screen
     */
    public static int getScreenWidth(Context context){
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * This method used to get the screen height
     * @param context to get the Application Context
     * @return int - height of the screen
     */
    public static int getScreenHeight(Context context){
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * This method is used check the internet connection
     * @param context to get the Application Context
     * @return
     */
    public static boolean isConnectingToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    /**
     * retuns the Desired date/time format
     */
    public static String getFormattedDate(long millis){
//        String pattern = "dd MMM , EEE - hh:mm aa";
        String pattern = "dd MMM , hh:mm aa";
        // Creating date format
        DateFormat simple = new SimpleDateFormat(pattern);

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(millis);
        return simple.format(result);
    }

    /**
     * retuns the Desired date/time format
     */
    public static String getFormattedBookDate(long millis){
        String pattern = "EEE, dd MMM - hh:mm aa";
//        String pattern = "dd MMM , hh:mm aa";
        // Creating date format
        DateFormat simple = new SimpleDateFormat(pattern);

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(millis);
        return simple.format(result);
    }


    /**
     * retuns the Desired date/time format
     */
    public static String getFormattedCompeltedDate(long millis){
        String pattern = "dd MMM YYYY, EEE - hh:mm aa";
        // Creating date format
        DateFormat simple = new SimpleDateFormat(pattern);

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(millis);
        return simple.format(result);
    }

    /**
     * retuns the Desired date/time format
     */
    public static String getDayOfTheWeek(long millis){
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(new Date(millis));

        int dow = cal.get(Calendar.DAY_OF_WEEK);

        switch (dow) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
        }
        return "";
    }

    public static void showErrorMessage(
            Context context , JSONObject jsonObject
    ){
        try {
//            JSONObject mJsonObject  = new JSONObject(response.errorBody().string());
            String errorMessage = jsonObject.getString("message");
            Toast.makeText(context , errorMessage , Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
