package com.ns.stellarjet.personalize

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityPersonalizeLaunchBinding
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants

class PersonalizeLaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // obtain binding
        val binding : ActivityPersonalizeLaunchBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_personalize_launch)
        //TODO remove dummy Booking ID
        SharedPreferencesHelper.saveBookingId(this , "9")

        binding.textViewPersonalizeCabPreferences.setOnClickListener {
            val mCabPreferencesIntent = Intent(
                this ,
                CabPreferencesActivity::class.java
            )
            startActivity(mCabPreferencesIntent)
        }

        binding.textViewPersonalizeFoodPreferences.setOnClickListener {
            val mFoodPreferencesIntent = Intent(
                this ,
                FoodPreferencesLaunchActivity::class.java
            )
            startActivity(mFoodPreferencesIntent)
        }

        binding.buttonPersonalizeLater.setOnClickListener {
            launchHome()
        }


    }

    fun launchHome(){
        val mHomeIntent = Intent(
            this ,
            HomeActivity::class.java
        )
        mHomeIntent.putExtra(UIConstants.BUNDLE_USER_DATA , HomeActivity.sUserData)
        startActivity(mHomeIntent)
        finish()
    }
}
