package com.ns.stellarjet.booking;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityFromBinding;

public class PlaceSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from);

        // obtain DataBinding
        ActivityFromBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_from);

        viewDataBinding.buttonFromBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_container ,  new FromFragment())
                .commit();

    }
}
