package com.ns.stellarjet.booking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ns.stellarjet.R;
import com.ns.stellarjet.databinding.ActivityFromBinding;
import com.ns.stellarjet.utils.SharedPreferencesHelper;

public class PlaceSelectionActivity extends AppCompatActivity {

    public static boolean isFromFragmentVisible = false;
    public static boolean isToFragmentVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain DataBinding
        ActivityFromBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_from);

        SharedPreferencesHelper.saveFoodPersonalize(this , false);
        SharedPreferencesHelper.saveCabPersonalize(this , false);
        viewDataBinding.buttonFromBack.setOnClickListener(v -> onBackPressed());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout_container ,  new FromFragment())
                .commit();


    }


}
