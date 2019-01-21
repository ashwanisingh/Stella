package com.ns.stellarjet.home

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
        @JvmField var mSeatNamesId : MutableList<Int> = ArrayList()
        @JvmField var mSeatNames : MutableList<String> = ArrayList()

        fun clearAllBookingData(){
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
            SharedPreferencesHelper.saveFromCityId(this@HomeActivity ,sUserData.locked_seats!![0].from_city!!)
            SharedPreferencesHelper.saveToCityId(this@HomeActivity ,sUserData.locked_seats!![0].to_city!!)
            SharedPreferencesHelper.saveJourneyDate(this@HomeActivity ,sUserData.locked_seats!![0].journey_date!!)
            SharedPreferencesHelper.saveJourneyTime(this@HomeActivity ,sUserData.locked_seats!![0].journey_time!!)
            SharedPreferencesHelper.saveJourneyTimeImMillis(this@HomeActivity ,sUserData.locked_seats!![0].datetime_ms!!)
            SharedPreferencesHelper.saveArrivalTime(this@HomeActivity ,sUserData.locked_seats!![0].arrival_time)
            SharedPreferencesHelper.saveFlightId(this@HomeActivity ,sUserData.locked_seats!![0].flight_id!!)

            sUserData.locked_seats!!.forEach {
                mSeatNamesId.add(it.flight_seat_id!!)
            }
            sUserData.locked_seats!!.forEach {
                mSeatNames.add(it.flight_seat!!.seat_code!!)
            }

            launchDialog()
        }

        /* set the username only if he is primary*/
        /*if(SharedPreferencesHelper.getUserType(this)!!.equals("primary" , ignoreCase = true)){
            activityHomeBinding.textViewHomeUserName.visibility = View.VISIBLE
            activityHomeBinding.textViewHomeUserName.text = sUserData.name
            activityHomeBinding.textViewHomeSeeAgain.visibility = View.GONE
        }else if(SharedPreferencesHelper.getUserType(this)!!.equals("secondary" , ignoreCase = true)){
            activityHomeBinding.textViewHomeUserName.visibility = View.GONE
            activityHomeBinding.textViewHomeSeeAgain.visibility = View.VISIBLE
        }*/

        val displayName = resources.getString(R.string.home_title_hello_again) + sUserData.name+ ","+
                resources.getString(R.string.home_title_welcome_back)
        activityHomeBinding.textviewHomeHelloAgain.text = displayName


        /* set the seats limit  */
        val seatsAvailable = sUserData.customer_prefs.seats_available
        val seatsRemaining: SpannableString
        activityHomeBinding.textViewSeatLimits.visibility = View.VISIBLE
        val displaySeats = resources.getString(R.string.home_remaining_seats_first_half) + " " +
                seatsAvailable + " "+
                resources.getString(R.string.home_remaining_seats_second_half)
        activityHomeBinding.textViewSeatLimits.text = displaySeats


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
            if(it.id == SharedPreferencesHelper.getFromCityId(this)){
                lockedFromCity = it.name
            }
        }
        sUserData.cities.forEach {
            if(it.id == SharedPreferencesHelper.getToCityId(this)){
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
                    mSeatsIntent.putExtra("flowFrom", "home")
                    startActivity(mSeatsIntent)
                } else if (numOfSeats == 12) {
                    val mSeatsIntent = Intent(this@HomeActivity, SeatSelectionActivity::class.java)
                    mSeatsIntent.putExtra("direction", sUserData.locked_seats!![0].direction)
                    mSeatsIntent.putExtra("sunRiseSet", sUserData.locked_seats!![0].sun_rise_set)
                    mSeatsIntent.putExtra("flowFrom", "home")
                    startActivity(mSeatsIntent)
                }

            }
        }

/*        alertDialogBuilder.setNegativeButton("Cancel") { _id, _ ->
            unlockSeats()
            _id.dismiss()

        }*/
        val stateMaintenenceDialog = alertDialogBuilder.create()
        stateMaintenenceDialog.setCanceledOnTouchOutside(false)
        stateMaintenenceDialog.setCancelable(false)



        stateMaintenenceDialog.setOnShowListener {
            stateMaintenenceDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorButtonNew))
            stateMaintenenceDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorButtonNew))
        }

        stateMaintenenceDialog.show()
    }


    private fun unlockSeats() {
        val progress = Progress.getInstance()
        progress.showProgress(this@HomeActivity)
        val mFlightSeatsConfirmCall = RetrofitAPICaller.getInstance(this@HomeActivity)
            .stellarJetAPIs.confirmFlightSeats(
            SharedPreferencesHelper.getUserToken(this@HomeActivity),
            SharedPreferencesHelper.getFlightId(this@HomeActivity),
            SharedPreferencesHelper.getUserId(this@HomeActivity),
            SharedPreferencesHelper.getFromCityId(this@HomeActivity),
            SharedPreferencesHelper.getToCityId(this@HomeActivity),
            SharedPreferencesHelper.getJourneyDate(this@HomeActivity),
            SharedPreferencesHelper.getJourneyTime(this@HomeActivity),
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
