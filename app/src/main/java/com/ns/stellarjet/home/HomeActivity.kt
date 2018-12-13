package com.ns.stellarjet.home

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.UserData
import com.ns.stellarjet.R
import com.ns.stellarjet.booking.PlaceSelectionActivity
import com.ns.stellarjet.databinding.ActivityHomeBinding
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants

class HomeActivity : AppCompatActivity() {

    companion object {
        lateinit var sUserData: UserData
        @JvmField var fromCityId : Int = 0
        @JvmField var toCityId : Int = 0
        @JvmField var fromCity : String = ""
        @JvmField var toCity : String = ""
        @JvmField var journeyTime : String = ""
        @JvmField var journeyDate : String = ""
        @JvmField var arrivalTime : String = ""
        @JvmField var flightId : Int = 0
        @JvmField var mSeatNamesId : List<Int> = ArrayList()
        @JvmField var mSeatNames : List<String> = ArrayList()

        fun clearAllBookingData(){
            fromCity = ""
            fromCityId = 0
            toCity = ""
            toCityId = 0
            journeyDate = ""
            journeyTime = ""
            arrivalTime = ""
            flightId = 0
            mSeatNames = ArrayList()
            mSeatNamesId = ArrayList()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityHomeBinding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )

        sUserData = intent.extras?.getParcelable(UIConstants.BUNDLE_USER_DATA)!!

        /* set the username only if he is primary*/
        if(SharedPreferencesHelper.getUserType(this)!!.equals("primary" , ignoreCase = true)){
            activityHomeBinding.textViewHomeUserName.visibility = View.VISIBLE
            activityHomeBinding.textViewHomeUserName.text = sUserData.name
            activityHomeBinding.textViewHomeSeeAgain.visibility = View.GONE
        }else if(SharedPreferencesHelper.getUserType(this)!!.equals("secondary" , ignoreCase = true)){
            activityHomeBinding.textViewHomeUserName.visibility = View.GONE
            activityHomeBinding.textViewHomeSeeAgain.visibility = View.VISIBLE
        }

        /* set the seats limit  */
        val seatsAvailable = sUserData.customer_prefs.seats_available
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

        /* launch the Booking flow */
        activityHomeBinding.buttonBookFlight.setOnClickListener {
            val mPlaceSelectionIntent = Intent(
                this ,
                PlaceSelectionActivity::class.java
            )
//            mPlaceSelectionIntent.putExtra(UIConstants.BUNDLE_USER_DATA , userData)
            startActivity(mPlaceSelectionIntent)
        }
    }
}
