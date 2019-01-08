package com.ns.stellarjet.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityBookingConfirmedBinding;
import com.ns.stellarjet.home.HomeActivity;
import com.ns.stellarjet.personalize.PersonalizeLaunchActivity;
import com.ns.stellarjet.utils.SharedPreferencesHelper;
import com.ns.stellarjet.utils.UIConstants;

public class BookingConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtain Binding
        ActivityBookingConfirmedBinding activityBookingConfirmedBinding =  DataBindingUtil.setContentView(
                this ,
                R.layout.activity_booking_confirmed);

        String fromCity = getIntent().getExtras().getString(UIConstants.BUNDLE_FROM_CITY);
        String toCity = getIntent().getExtras().getString(UIConstants.BUNDLE_TO_CITY);

        activityBookingConfirmedBinding.buttonHome.setOnClickListener(v -> {
            String bookingId = SharedPreferencesHelper.getBookingId(BookingConfirmedActivity.this);
            Log.d("BookingConfirmed", "onCreate: " +bookingId);
            SharedPreferencesHelper.saveBookingId(
                    BookingConfirmedActivity.this ,
                    ""
                    );
            Intent mIntent = new Intent(
                    BookingConfirmedActivity.this ,
                    HomeActivity.class
            );
            mIntent.putExtra(UIConstants.BUNDLE_USER_DATA , HomeActivity.sUserData);
            startActivity(mIntent);
            finish();
        });

        activityBookingConfirmedBinding.buttonConfirmedPersonalize.setOnClickListener(v -> {
            Intent mIntent = new Intent(
                    BookingConfirmedActivity.this ,
                    PersonalizeLaunchActivity.class
            );
            mIntent.putExtra(UIConstants.BUNDLE_FROM_CITY, fromCity);
            mIntent.putExtra(UIConstants.BUNDLE_TO_CITY , toCity);
            startActivity(mIntent);
            finish();
        });


    }
}
