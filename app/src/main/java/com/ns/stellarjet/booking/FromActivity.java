package com.ns.stellarjet.booking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ns.stellarjet.R;

public class FromActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_container ,  new FromFragment())
                .commit();

    }
}
