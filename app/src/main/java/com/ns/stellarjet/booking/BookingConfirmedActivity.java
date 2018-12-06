package com.ns.stellarjet.booking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityBookingConfirmedBinding;

public class BookingConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtain Binding
        ActivityBookingConfirmedBinding activityBookingConfirmedBinding =  DataBindingUtil.setContentView(this , R.layout.activity_booking_confirmed);

        activityBookingConfirmedBinding.buttonHome.setOnClickListener(v -> {

        });

        activityBookingConfirmedBinding.buttonConfirmedPersonalize.setOnClickListener(v -> {

        });


    }
}
