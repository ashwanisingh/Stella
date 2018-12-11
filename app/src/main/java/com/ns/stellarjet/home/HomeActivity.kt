package com.ns.stellarjet.home

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.UserData
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityHomeBinding
import com.ns.stellarjet.utils.UIConstants

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityHomeBinding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )

        val userData = intent.extras?.getParcelable<UserData>(UIConstants.BUNDLE_USER_DATA)
        Log.d("Home", "onCreate: " + userData!!)

        val seatsAvailable = userData.customer_prefs.seats_available
        activityHomeBinding.textViewSeatLimits.visibility = View.VISIBLE
        activityHomeBinding.textViewSeatLimits.text =
                resources.getString(R.string.home_remaining_seats_first_half)
        if(seatsAvailable<4){
            val seatsRemaining = SpannableString(" $seatsAvailable seats ")
            seatsRemaining.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this , R.color.colorCreditAlert)),
                0,
                seatsRemaining.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            activityHomeBinding.textViewSeatLimits.append(seatsRemaining)
        }else{
            activityHomeBinding.textViewSeatLimits.append(" "+ seatsAvailable.toString()+" seats ")
        }
        activityHomeBinding.textViewSeatLimits.append(resources.getString(R.string.home_remaining_seats_second_half))

    }
}
