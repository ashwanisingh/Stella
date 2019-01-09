package com.ns.stellarjet.home

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.FlightSeatsConfirmResponse
import com.ns.networking.model.UserData
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.booking.PlaceSelectionActivity
import com.ns.stellarjet.booking.SeatLayoutOneSelectionActivity
import com.ns.stellarjet.booking.SeatSelectionActivity
import com.ns.stellarjet.databinding.ActivityHomeBinding
import com.ns.stellarjet.drawer.DrawerActivity
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    companion object {
        lateinit var sUserData: UserData
        @JvmField var fromCityId : Int = 0
        @JvmField var toCityId : Int = 0
        @JvmField var fromCity : String = ""
        @JvmField var toCity : String = ""
        @JvmField var journeyTime : String = ""
        @JvmField var journeyDate : String = ""
        @JvmField var journeyTimeInMillis : Long = 0
        @JvmField var arrivalTime : String = ""
        @JvmField var flightId : Int = 0
        @JvmField var mSeatNamesId : MutableList<Int> = ArrayList()
        @JvmField var mSeatNames : MutableList<String> = ArrayList()

        fun clearAllBookingData(){
            fromCity = ""
            fromCityId = 0
            toCity = ""
            toCityId = 0
            journeyDate = ""
            journeyTime = ""
            journeyTimeInMillis = 0
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

//        set locked seats data and pass to Seat layout Activity
        if(sUserData.locked_seats?.isNotEmpty()!!){
            fromCityId = sUserData.locked_seats!![0].from_city!!
            toCityId = sUserData.locked_seats!![0].to_city!!
            journeyDate = sUserData.locked_seats!![0].journey_date!!
            journeyTime = sUserData.locked_seats!![0].journey_time!!
            journeyTimeInMillis = sUserData.locked_seats!![0].datetime_ms!!
            arrivalTime = sUserData.locked_seats!![0].arrival_time!!
            flightId = sUserData.locked_seats!![0].flight_id!!
            sUserData.locked_seats!!.forEach {
                mSeatNamesId.add(it.flight_seat_id!!)
            }
            sUserData.locked_seats!!.forEach {
                mSeatNames.add(it.flight_seat!!.seat_code)
            }

            launchDialog()
        }

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
        val seatsRemaining: SpannableString
        activityHomeBinding.textViewSeatLimits.visibility = View.VISIBLE
        if(seatsAvailable > 5){
            activityHomeBinding.textViewSeatLimits.text =
                    resources.getString(R.string.home_remaining_seats_first_half)
            seatsRemaining = SpannableString(" $seatsAvailable seats ")
            seatsRemaining.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this , R.color.colorLoginButton)),
                0,
                seatsRemaining.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }else{
            activityHomeBinding.textViewSeatLimits.text =
                    resources.getString(R.string.home_remaining_seats_first_half)
            seatsRemaining = SpannableString(" $seatsAvailable seats ")
            seatsRemaining.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this , R.color.colorCreditAlert)),
                0,
                seatsRemaining.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        activityHomeBinding.textViewSeatLimits.append(seatsRemaining)
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

        /* launch DrawerActivity */
        activityHomeBinding.buttonDrawer.setOnClickListener {
            val mDrawerActivtyIntent = Intent(
                this ,
                DrawerActivity::class.java
            )
//            mPlaceSelectionIntent.putExtra(UIConstants.BUNDLE_USER_DATA , userData)
            startActivity(mDrawerActivtyIntent)
        }
    }

    private fun launchDialog(){

        var lockedFromCity = ""
        var lockedToCity = ""
        sUserData.cities.forEach {
            if(it.id == fromCityId){
                lockedFromCity = it.name
            }
        }
        sUserData.cities.forEach {
            if(it.id == toCityId){
                lockedToCity = it.name
            }
        }
        val numOfSeats = sUserData.locked_seats!![0].flight?.no_of_seats

        var seatNames  = ""

        mSeatNames.forEach {
            seatNames = if(seatNames.isEmpty()){
                it
            }else{
                "$seatNames , $it"
            }
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Previous booking")
        alertDialogBuilder.setMessage("You have already locked seats $seatNames to go from $lockedFromCity to $lockedToCity" )
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton("Ok") { _, _ ->
            run {
                if (numOfSeats == 8) {
                    val mSeatsIntent = Intent(this@HomeActivity, SeatLayoutOneSelectionActivity::class.java)
                    mSeatsIntent.putExtra("direction", sUserData.locked_seats!![0].direction)
                    mSeatsIntent.putExtra("sunRiseSet", sUserData.locked_seats!![0].sun_rise_set)
                    startActivity(mSeatsIntent)
                } else if (numOfSeats == 12) {
                    val mSeatsIntent = Intent(this@HomeActivity, SeatSelectionActivity::class.java)
                    mSeatsIntent.putExtra("direction", sUserData.locked_seats!![0].direction)
                    mSeatsIntent.putExtra("sunRiseSet", sUserData.locked_seats!![0].sun_rise_set)
                    startActivity(mSeatsIntent)
                }

            }
        }

        alertDialogBuilder.setNegativeButton("Cancel") { _id, _ ->
            unlockSeats()
            _id.dismiss()

        }


        val alert11 = alertDialogBuilder.create()
        alert11.show()
        alert11.setCanceledOnTouchOutside(false)
        alert11.setCancelable(false)

    }


    private fun unlockSeats() {
        val progress = Progress.getInstance()
        progress.showProgress(this@HomeActivity)
        val mFlightSeatsConfirmCall = RetrofitAPICaller.getInstance(this@HomeActivity)
            .stellarJetAPIs.confirmFlightSeats(
            SharedPreferencesHelper.getUserToken(this@HomeActivity),
            HomeActivity.flightId,
            SharedPreferencesHelper.getUserId(this@HomeActivity),
            HomeActivity.fromCityId,
            HomeActivity.toCityId,
            HomeActivity.journeyDate,
            HomeActivity.journeyTime,
            HomeActivity.mSeatNamesId,
            null
        )

        mFlightSeatsConfirmCall.enqueue(object : Callback<FlightSeatsConfirmResponse> {
            override fun onResponse(
                call: Call<FlightSeatsConfirmResponse>,
                response: Response<FlightSeatsConfirmResponse>) {
                progress.hideProgress()
                if (response.body() != null) {
                    Log.d("Booking", "onResponse: " + response.body()!!)
                    if(response.code() == 200){
                        Toast.makeText(this@HomeActivity , "Seats are unlocked" , Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<FlightSeatsConfirmResponse>, t: Throwable) {
                progress.hideProgress()
                Log.d("Booking", "onFailure: $t")
                Toast.makeText(this@HomeActivity, "Server Error Occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
