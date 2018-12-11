package com.ns.stellarjet.home;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.ns.networking.model.UserData;
import com.ns.stellarjet.R;
import com.ns.stellarjet.utils.UIConstants;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UserData userData = Objects.requireNonNull(getIntent().getExtras()).getParcelable(UIConstants.BUNDLE_USER_DATA);
        Log.d("Home", "onCreate: " + userData);
    }
}
