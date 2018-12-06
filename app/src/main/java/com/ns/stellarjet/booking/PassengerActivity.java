package com.ns.stellarjet.booking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityPassengerBinding;

public class PassengerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        ActivityPassengerBinding activityPassengerBinding = DataBindingUtil.setContentView(this, R.layout.activity_passenger);
        activityPassengerBinding.buttonPassengerBack.setOnClickListener(v -> onBackPressed());
    }
}
