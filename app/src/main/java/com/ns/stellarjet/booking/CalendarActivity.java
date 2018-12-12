package com.ns.stellarjet.booking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.networking.model.FlightScheduleData;
import com.ns.networking.model.FlightScheduleResponse;
import com.ns.networking.retrofit.RetrofitAPICaller;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityCalendarBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalendarActivity extends AppCompatActivity {

    private List<FlightScheduleData> mFlightScheduleDataList;
    private List<Integer> mDates = new ArrayList<>();
    private ActivityCalendarBinding activityCalendarBinding;
    private int selectedIndex ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtain Binding
        activityCalendarBinding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);

        getFlightSchedules();

        activityCalendarBinding.buttonScheduleBack.setOnClickListener(v -> onBackPressed());

        activityCalendarBinding.textViewCalendarFrom.setText(HomeActivity.fromCity);
        activityCalendarBinding.textViewCalendarTo.setText(HomeActivity.toCity);

        activityCalendarBinding.buttonScheduleConfirmDate.setOnClickListener(v -> {
            if(selectedIndex == -1){
                Toast.makeText(CalendarActivity.this, "No Flights are available", Toast.LENGTH_SHORT).show();
            }else {
                HomeActivity.journeyDate = mFlightScheduleDataList.get(selectedIndex).getJourney_date();
                HomeActivity.journeyTime = mFlightScheduleDataList.get(selectedIndex).getJourney_time();
                HomeActivity.journeyTimeinMillis = mFlightScheduleDataList.get(selectedIndex).getJourney_datetime_ms();
                HomeActivity.flightId = mFlightScheduleDataList.get(selectedIndex).getFlight_id();
                String direction = mFlightScheduleDataList.get(selectedIndex).getDirection();
                String sunRiseSet = mFlightScheduleDataList.get(selectedIndex).getSun_rise_set();
                int numSeats = mFlightScheduleDataList.get(selectedIndex).getFlight_seat_availability().getTotal_seats();
//                Toast.makeText(CalendarActivity.this, numSeats+"", Toast.LENGTH_SHORT).show();
                if(numSeats == 8){
                    Intent mSeatsIntent = new Intent(CalendarActivity.this , SeatLayoutOneSelectionActivity.class);
                    mSeatsIntent.putExtra("direction" , direction);
                    mSeatsIntent.putExtra("sunRiseSet" , sunRiseSet);
                    startActivity(mSeatsIntent);
                }else if(numSeats == 12){
                    Intent mSeatsIntent = new Intent(CalendarActivity.this , SeatSelectionActivity.class);
                    mSeatsIntent.putExtra("direction" , direction);
                    mSeatsIntent.putExtra("sunRiseSet" , sunRiseSet);
                    startActivity(mSeatsIntent);
                }
            }
        });
    }

    private void getFlightSchedules(){
        Call<FlightScheduleResponse> mFlightScheduleResponseCall = RetrofitAPICaller.getInstance(CalendarActivity.this)
                .getStellarJetAPIs().getFlightSchedules(
                        SharedPreferencesHelper.getUserToken(CalendarActivity.this) ,
                        String.valueOf(HomeActivity.fromCityId) ,
                        String.valueOf(HomeActivity.toCityId) ,
                        "90"
                );

        mFlightScheduleResponseCall.enqueue(new Callback<FlightScheduleResponse>() {
            @Override
            public void onResponse(@NonNull Call<FlightScheduleResponse> call, @NonNull Response<FlightScheduleResponse> response) {
                Log.d("Calendar", "onResponse: " + response.body());
                if (response.body() != null) {
                    mFlightScheduleDataList = response.body().getData();
                    setCalendar();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlightScheduleResponse> call, @NonNull Throwable t) {
                Log.d("Calendar", "onFailure: " + t);
            }
        });
    }

    private void setCalendar(){

        for (int i = 0; i < mFlightScheduleDataList.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mFlightScheduleDataList.get(i).getJourney_datetime_ms());
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            mDates.add(day);
        }

        // setting the minimum data as current date
        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -1);
        activityCalendarBinding.calendarView.setMinimumDate(min);

        // setting the maximum date to 90
        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 88);
        activityCalendarBinding.calendarView.setMaximumDate(max);

        // make the schedule days list from API
        List<Calendar> mScheduleDaysList = new ArrayList<>();
        for (int i = 0; i < mFlightScheduleDataList.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mFlightScheduleDataList.get(i).getJourney_datetime_ms());
            int date = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            Calendar mTempCalendar  = Calendar.getInstance();
            mTempCalendar.set(Calendar.DAY_OF_MONTH , date);
            mTempCalendar.set(Calendar.MONTH , month);
            mTempCalendar.set(Calendar.YEAR , year);
            mTempCalendar.set(Calendar.HOUR, 0);
            mTempCalendar.set(Calendar.MINUTE, 0);
            mTempCalendar.set(Calendar.SECOND , 0);
            mTempCalendar.set(Calendar.MILLISECOND , 0);
            mScheduleDaysList.add(mTempCalendar);
        }

        long noOfDays = daysBetween(min, max);

        // make a list for whole 90 days
        List<Calendar> mTotalDays = new ArrayList<>();
        for (int i = 0; i < noOfDays; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, i);
            calendar.set(Calendar.HOUR , 0);
            calendar.set(Calendar.MINUTE , 0);
            calendar.set(Calendar.SECOND , 0);
            calendar.set(Calendar.MILLISECOND , 0);
            mTotalDays.add(calendar);
        }
        // remove the schdeuled days from total days
        mTotalDays.removeAll(mScheduleDaysList);
        // set the total days list as disabled days
        activityCalendarBinding.calendarView.setDisabledDays(mTotalDays);

        activityCalendarBinding.calendarView.setOnDayClickListener(eventDay -> {
            String pattern = "dd MMM , EEE - hh:mm aa";
            if(eventDay.isEnabled()){
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                dateFormat.setTimeZone(eventDay.getCalendar().getTimeZone());
                if(mDates.contains(eventDay.getCalendar().getTime().getDate())){
                    int index = mDates.indexOf(eventDay.getCalendar().getTime().getDate());
                    selectedIndex = index;
                    activityCalendarBinding.textViewScheduleDate.setText(
                            StellarJetUtils.getFormattedBookDate(
                                    mFlightScheduleDataList.get(selectedIndex).getJourney_datetime_ms()));
                    activityCalendarBinding.textViewScheduleSeatsAvailable.setVisibility(View.VISIBLE);
                    String seatsAvailable = mFlightScheduleDataList.get(index).getFlight_seat_availability()
                            .getAvailable_seats() +" seats available";
                    activityCalendarBinding.textViewScheduleSeatsAvailable.setText(seatsAvailable);
                    activityCalendarBinding.buttonScheduleConfirmDate.setEnabled(true);
                    activityCalendarBinding.buttonScheduleConfirmDate.setAlpha(1.0f);
                }else{
                    activityCalendarBinding.textViewScheduleSeatsAvailable.setVisibility(View.INVISIBLE);
                    activityCalendarBinding.textViewScheduleDate.setText(
                            getResources().getString(R.string.booking_date_seats_no_flights)
                    );
                    selectedIndex = -1;
                    activityCalendarBinding.buttonScheduleConfirmDate.setEnabled(false);
                    activityCalendarBinding.buttonScheduleConfirmDate.setAlpha(0.5f);
                }
            }

        });
    }

    /* to calculate the days between two dates*/
    private long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }
}
