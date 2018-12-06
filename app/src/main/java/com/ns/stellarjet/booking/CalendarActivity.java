package com.ns.stellarjet.booking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityCalendarBinding;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtain Binding
        ActivityCalendarBinding activityCalendarBinding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);

        activityCalendarBinding.buttonScheduleBack.setOnClickListener(v -> onBackPressed());

        activityCalendarBinding.buttonScheduleConfirmDate.setOnClickListener(v -> {

        });
    }
}
