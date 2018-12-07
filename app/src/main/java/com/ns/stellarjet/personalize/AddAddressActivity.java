package com.ns.stellarjet.personalize;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityAddAddressBinding;

public class AddAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAddAddressBinding  activityAddAddressBinding = DataBindingUtil.
                setContentView(this, R.layout.activity_add_address);

        activityAddAddressBinding.buttonAddAddressBack.setOnClickListener(v -> {
            onBackPressed();
        });

        activityAddAddressBinding.buttonAddAddressConfirm.setOnClickListener(v -> {
            finish();
        });

    }
}
