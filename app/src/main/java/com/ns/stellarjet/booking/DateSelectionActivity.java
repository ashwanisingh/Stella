package com.ns.stellarjet.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.ns.networking.model.FlightScheduleData;
import com.ns.stellarjet.R;
import com.ns.stellarjet.booking.adapter.CalendarDaysAdapter;
import com.ns.stellarjet.booking.adapter.WeekDaysAdapter;
import com.ns.stellarjet.databinding.ActivityDateSelectionBinding;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.StellarJetUtils;
import com.ns.stellarjet.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateSelectionActivity extends AppCompatActivity implements CalendarDaysAdapter.onDateSelectClickListener {

    private ActivityDateSelectionBinding mActivityDateSelectionBinding;
    private List<FlightScheduleData> mFlightScheduleDataList;
    private List<Calendar> mCalendarList = new ArrayList<>();
    private List<Calendar> mScheduledCalendarList = new ArrayList<>();
    private int currentMonth =0;
    private int initialMonth =0;
    private int lastMonth = 0;
    private int selectedIndex ;
    private List<String> mDaysList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_date_selection);

        mActivityDateSelectionBinding = DataBindingUtil.setContentView(this, R.layout.activity_date_selection);
        mFlightScheduleDataList = Objects.requireNonNull(getIntent().getExtras()).getParcelableArrayList("dates");

        Calendar mCalendar = Calendar.getInstance();
        currentMonth = mCalendar.get(Calendar.MONTH);
        initialMonth = mCalendar.get(Calendar.MONTH);
        mCalendar.add(Calendar.MONTH , 2);
        lastMonth = mCalendar.get(Calendar.MONTH);
        setScheduledCalendar();
        setDaysNameList();
        setMonthView();

        if(initialMonth == currentMonth){
            mActivityDateSelectionBinding.buttonLastMonth.setEnabled(false);
            mActivityDateSelectionBinding.buttonLastMonth.setAlpha(0.4f);
        }

        mActivityDateSelectionBinding.buttonScheduleBack.setOnClickListener(v -> onBackPressed());

        mActivityDateSelectionBinding.textViewCalendarFrom.setText(SharedPreferencesHelper.getFromCity(DateSelectionActivity.this));
        mActivityDateSelectionBinding.textViewCalendarTo.setText(SharedPreferencesHelper.getToCity(DateSelectionActivity.this));

        mActivityDateSelectionBinding.buttonScheduleConfirmDate.setOnClickListener(v -> {
            if(selectedIndex == -1){
                UiUtils.Companion.showSimpleDialog(
                        DateSelectionActivity.this, "No Flights are available"
                );
            }else {
                SharedPreferencesHelper.saveJourneyDate(
                        DateSelectionActivity.this ,
                        mFlightScheduleDataList.get(selectedIndex).getJourney_date());
                SharedPreferencesHelper.saveJourneyTime(
                        DateSelectionActivity.this ,
                        mFlightScheduleDataList.get(selectedIndex).getJourney_time());
                SharedPreferencesHelper.saveJourneyTimeImMillis(
                        DateSelectionActivity.this ,
                        mFlightScheduleDataList.get(selectedIndex).getJourney_datetime_ms());
                SharedPreferencesHelper.saveArrivalTime(
                        DateSelectionActivity.this ,
                        mFlightScheduleDataList.get(selectedIndex).getArrival_time());
                SharedPreferencesHelper.saveScheduleId(
                        DateSelectionActivity.this ,
                        mFlightScheduleDataList.get(selectedIndex).getSchedule_id());
                SharedPreferencesHelper.saveFlightId(
                        DateSelectionActivity.this ,
                        mFlightScheduleDataList.get(selectedIndex).getFlight_id());
                String direction = mFlightScheduleDataList.get(selectedIndex).getDirection();
                String sunRiseSet = mFlightScheduleDataList.get(selectedIndex).getSun_rise_set();
                int numSeats = mFlightScheduleDataList.get(selectedIndex).getFlight_seat_availability().getTotal_seats();
//                Toast.makeText(DateSelectionActivity.this, numSeats+"", Toast.LENGTH_SHORT).show();
                if(numSeats == 8){
                    Intent mSeatsIntent = new Intent(DateSelectionActivity.this , SeatLayoutOneSelectionActivity.class);
                    mSeatsIntent.putExtra("direction" , direction);
                    mSeatsIntent.putExtra("sunRiseSet" , sunRiseSet);
                    mSeatsIntent.putExtra("flowFrom", "calendar");
                    startActivity(mSeatsIntent);
                }else if(numSeats == 12){
                    Intent mSeatsIntent = new Intent(DateSelectionActivity.this , SeatSelectionActivity.class);
                    mSeatsIntent.putExtra("direction" , direction);
                    mSeatsIntent.putExtra("sunRiseSet" , sunRiseSet);
                    mSeatsIntent.putExtra("flowFrom", "calendar");
                    startActivity(mSeatsIntent);
                }
            }
        });

        mActivityDateSelectionBinding.buttonNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityDateSelectionBinding.buttonLastMonth.setEnabled(true);
                mActivityDateSelectionBinding.buttonLastMonth.setAlpha(1.0f);
                if(currentMonth!=lastMonth){
                    currentMonth = currentMonth + 1;
                    setMonthView();
                }
                if(currentMonth == lastMonth){
                    mActivityDateSelectionBinding.buttonNextMonth.setEnabled(false);
                    mActivityDateSelectionBinding.buttonNextMonth.setAlpha(0.4f);
                }
            }
        });

        mActivityDateSelectionBinding.buttonLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityDateSelectionBinding.buttonNextMonth.setEnabled(true);
                mActivityDateSelectionBinding.buttonNextMonth.setAlpha(1.0f);
                if(initialMonth!=currentMonth){
                    currentMonth = currentMonth - 1;
                    setMonthView();
                }
                if(initialMonth == currentMonth){
                    mActivityDateSelectionBinding.buttonLastMonth.setEnabled(false);
                    mActivityDateSelectionBinding.buttonLastMonth.setAlpha(0.4f);
                    /*mActivityDateSelectionBinding.buttonLastMonth.setEnabled(true);
                    mActivityDateSelectionBinding.buttonLastMonth.setAlpha(1.0f);*/
                }
            }
        });
    }

    private void setScheduledCalendar(){
/*        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.MONTH , currentMonth);
        int numOfDays = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < numOfDays; i++) {
            if(i%2==0){
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(Calendar.DAY_OF_MONTH , i*2);
                tempCalendar.set(Calendar.MONTH , currentMonth);
                tempCalendar.set(Calendar.HOUR, 0);
                tempCalendar.set(Calendar.MINUTE, 0);
                tempCalendar.set(Calendar.SECOND , 0);
                tempCalendar.set(Calendar.MILLISECOND , 0);
                mScheduledCalendarList.add(tempCalendar);
            }
        }*/
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
            mScheduledCalendarList.add(mTempCalendar);
        }
        Log.d("Date", "setScheduledCalendar: " + mScheduledCalendarList);
    }

    private void setMonthView(){
        mActivityDateSelectionBinding.textViewMonth.setText(getCurrentMonthName());
        mCalendarList.clear();
        String firstDayinMonth = getFirstDayInWeek();
        int daysToPostpone = getFirstDayDelay(firstDayinMonth.toUpperCase());
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.MONTH , currentMonth);
        int numOfDays = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

//        Toast.makeText(this, ""+numOfDays, Toast.LENGTH_SHORT).show();

        for (int i = 0; i < numOfDays; i++) {
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.set(Calendar.MONTH , currentMonth);
            tempCalendar.set(Calendar.DAY_OF_MONTH , i+1);
            tempCalendar.set(Calendar.HOUR , 0);
            tempCalendar.set(Calendar.MINUTE , 0);
            tempCalendar.set(Calendar.SECOND , 0);
            tempCalendar.set(Calendar.MILLISECOND , 0);
            mCalendarList.add(tempCalendar);
        }

        CalendarDaysAdapter mCalendarDaysAdapter = new CalendarDaysAdapter(
                DateSelectionActivity.this,
                mCalendarList ,
                mScheduledCalendarList ,
                daysToPostpone
        );

        GridLayoutManager layoutManager = new GridLayoutManager(
                this ,
                7
        );

        mActivityDateSelectionBinding.recyclerViewCalendar.setAdapter(mCalendarDaysAdapter);
        mActivityDateSelectionBinding.recyclerViewCalendar.setLayoutManager(layoutManager);
    }

    private String getFirstDayInWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH , currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH , 1);
        Date date = calendar.getTime();
        /*System.out.println(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
        System.out.println()*/
        return  new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
    }

    private String getCurrentMonthName(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH , currentMonth);
        Date date = calendar.getTime();
        return  new SimpleDateFormat("MMM yyyy", Locale.ENGLISH).format(date.getTime());
    }

    private int getFirstDayDelay(String daysName){
        int day = 0;
        switch (daysName){
            case "SUNDAY":
                day = 0;
                break;
            case "MONDAY":
                day = 1;
                break;
            case "TUESDAY":
                day = 2;
                break;
            case "WEDNESDAY":
                day = 3;
                break;
            case "THURSDAY":
                day = 4;
                break;
            case "FRIDAY":
                day = 5;
                break;
            case "SATURDAY":
                day = 6;
                break;
        }
        return day;
    }

    private void setDaysNameList(){
        mDaysList.add("SUN");
        mDaysList.add("MON");
        mDaysList.add("TUE");
        mDaysList.add("WED");
        mDaysList.add("THU");
        mDaysList.add("FRI");
        mDaysList.add("SAT");

        WeekDaysAdapter mDaysAdapter = new WeekDaysAdapter(
                mDaysList
        );

        GridLayoutManager layoutManager = new GridLayoutManager(
                DateSelectionActivity.this,
                7
        );

        mActivityDateSelectionBinding.recyclerViewDays.setAdapter(mDaysAdapter);
        mActivityDateSelectionBinding.recyclerViewDays.setLayoutManager(layoutManager);
        mActivityDateSelectionBinding.recyclerViewDays.setLayoutFrozen(true);
    }

    @Override
    public void onDateSelected(Calendar calendar) {
        Date date = calendar.getTime();
        String selectedDate =  new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date.getTime());
//        String selectedDate = selectedYear +"-"+selectedMonth +"-"+selectedDay;
        for (int i = 0; i < mFlightScheduleDataList.size(); i++) {
            String tempDate  = mFlightScheduleDataList.get(i).getJourney_date();
            if(selectedDate.equalsIgnoreCase(tempDate)){
                selectedIndex = i;
                mActivityDateSelectionBinding.textViewScheduleDate.setVisibility(View.VISIBLE);
                mActivityDateSelectionBinding.textViewScheduleDate.setText(StellarJetUtils.getFormattedCalendarBookDate(
                        mFlightScheduleDataList.get(i).getJourney_datetime_ms()));
                String seatsAvailable = mFlightScheduleDataList.get(i).getFlight_seat_availability()
                        .getAvailable_seats() +" seats available";
                mActivityDateSelectionBinding.textViewScheduleSeatsAvailable.setText(seatsAvailable);
            }
        }
        /*mActivityDateSelectionBinding.textViewScheduleDate.setText(
                StellarJetUtils.getFormattedCalendarBookDate(
                        mScheduledCalendar.getTimeInMillis()));*/
        mActivityDateSelectionBinding.scrollviewCalendar.fullScroll(View.FOCUS_DOWN);
    }
}
