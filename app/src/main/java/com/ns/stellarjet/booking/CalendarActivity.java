package com.ns.stellarjet.booking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import butterknife.ButterKnife;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.ns.networking.model.FlightScheduleData;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityCalendarBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.utils.StellarJetUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CalendarActivity extends AppCompatActivity {

    private List<FlightScheduleData> mFlightScheduleDataList;
    private List<String> mDates = new ArrayList<>();
    private ActivityCalendarBinding mActivityCalendarBinding;
    private int selectedIndex ;
    private Calendar min;
    private Calendar max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(CalendarActivity.this);

        // obtain Binding
        mActivityCalendarBinding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);

        // setting the minimum data as current date
        min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -1);
        mActivityCalendarBinding.calendarView.setMinimumDate(min);

        // setting the maximum date to 90
        max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 88);
        mActivityCalendarBinding.calendarView.setMaximumDate(max);

        mFlightScheduleDataList = Objects.requireNonNull(getIntent().getExtras()).getParcelableArrayList("dates");

        setCalendar();

        mActivityCalendarBinding.buttonScheduleBack.setOnClickListener(v -> onBackPressed());

        mActivityCalendarBinding.textViewCalendarFrom.setText(HomeActivity.fromCity);
        mActivityCalendarBinding.textViewCalendarTo.setText(HomeActivity.toCity);

        mActivityCalendarBinding.buttonScheduleConfirmDate.setOnClickListener(v -> {
            if(selectedIndex == -1){
                Toast.makeText(CalendarActivity.this, "No Flights are available", Toast.LENGTH_SHORT).show();
            }else {
                HomeActivity.journeyDate = mFlightScheduleDataList.get(selectedIndex).getJourney_date();
                HomeActivity.journeyTime = mFlightScheduleDataList.get(selectedIndex).getJourney_time();
                HomeActivity.journeyTimeInMillis = mFlightScheduleDataList.get(selectedIndex).getJourney_datetime_ms();
                HomeActivity.arrivalTime = mFlightScheduleDataList.get(selectedIndex).getArrival_time();
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



    private void setCalendar(){

        for (int i = 0; i < mFlightScheduleDataList.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mFlightScheduleDataList.get(i).getJourney_datetime_ms());
            String date = StellarJetUtils.getFormattedCalendarDate(
                    calendar.getTimeInMillis());
            mDates.add(date);
        }

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
        mActivityCalendarBinding.calendarView.setDisabledDays(mTotalDays);

        if(mFlightScheduleDataList.size()>0){
            Calendar mFirstDayCalendar = Calendar.getInstance();
            mFirstDayCalendar.setTimeInMillis(mFlightScheduleDataList.get(0).getJourney_datetime_ms());
            try {
                mActivityCalendarBinding.calendarView.setDate(mFirstDayCalendar);
            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }
            mActivityCalendarBinding.calendarView.setPreviousButtonImage(null);
            mActivityCalendarBinding.textViewScheduleDate.setText(
                    StellarJetUtils.getFormattedBookDate(
                            mFlightScheduleDataList.get(0).getJourney_datetime_ms()));
            mActivityCalendarBinding.textViewScheduleDate.setVisibility(View.VISIBLE);
            String seatsAvailable = mFlightScheduleDataList.get(0).getFlight_seat_availability()
                    .getAvailable_seats() +" seats available";
            mActivityCalendarBinding.textViewScheduleSeatsAvailable.setText(seatsAvailable);
            mActivityCalendarBinding.buttonScheduleConfirmDate.setEnabled(true);
            mActivityCalendarBinding.buttonScheduleConfirmDate.setAlpha(1.0f);
        }

        mActivityCalendarBinding.calendarView.setOnDayClickListener(eventDay -> {
            String pattern = "dd MMM, EEE";
            if(eventDay.isEnabled()){
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                dateFormat.setTimeZone(eventDay.getCalendar().getTimeZone());
                String selectedDay = StellarJetUtils.getFormattedCalendarDate(eventDay.getCalendar().getTimeInMillis());
                if(mDates.contains(selectedDay)){
                    int index = mDates.indexOf(selectedDay);
                    selectedIndex = index;
                    mActivityCalendarBinding.textViewScheduleDate.setText(
                            StellarJetUtils.getFormattedBookDate(
                                    mFlightScheduleDataList.get(selectedIndex).getJourney_datetime_ms()));
                    mActivityCalendarBinding.textViewScheduleDate.setVisibility(View.VISIBLE);
                    String seatsAvailable = mFlightScheduleDataList.get(index).getFlight_seat_availability()
                            .getAvailable_seats() +" seats available";
                    mActivityCalendarBinding.textViewScheduleSeatsAvailable.setText(seatsAvailable);
                    mActivityCalendarBinding.buttonScheduleConfirmDate.setEnabled(true);
                    mActivityCalendarBinding.buttonScheduleConfirmDate.setAlpha(1.0f);
                }else{
                    mActivityCalendarBinding.textViewScheduleSeatsAvailable.setVisibility(View.INVISIBLE);
                    mActivityCalendarBinding.textViewScheduleDate.setText(
                            getResources().getString(R.string.booking_date_seats_no_flights)
                    );
                    selectedIndex = -1;
                    mActivityCalendarBinding.buttonScheduleConfirmDate.setEnabled(false);
                    mActivityCalendarBinding.buttonScheduleConfirmDate.setAlpha(0.5f);
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
