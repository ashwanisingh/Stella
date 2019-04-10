package com.ns.stellarjet.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.UpdateDeviceToken
import com.ns.networking.model.UserData
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.booking.CabinSeatActivity
import com.ns.stellarjet.booking.PlaceSelectionActivity
import com.ns.stellarjet.booking.SeatLayoutOneSelectionActivity
import com.ns.stellarjet.booking.SeatSelectionActivity
import com.ns.stellarjet.databinding.ActivityHomeBinding
import com.ns.stellarjet.drawer.DrawerActivity
import com.ns.stellarjet.drawer.PurchaseActivity
import com.ns.stellarjet.utils.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class HomeActivity : AppCompatActivity() {


    private lateinit var activityHomeBinding: ActivityHomeBinding

    public companion object {
        @JvmField var sUserData: UserData? = null
        @JvmField var mSeatNamesId : MutableList<Int> = ArrayList()
        @JvmField var mSeatNames : MutableList<String> = ArrayList()

        fun clearAllBookingData(){
            mSeatNames = ArrayList()
            mSeatNamesId = ArrayList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityHomeBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )


        sUserData = intent.extras?.getParcelable(UIConstants.BUNDLE_USER_DATA)!!

//        set locked seats data and pass to Seat layout Activity
        if(sUserData?.locked_seats?.isNotEmpty()!!){
            SharedPreferencesHelper.saveFromCityId(this@HomeActivity ,sUserData?.locked_seats!![0].from_city!!)
            SharedPreferencesHelper.saveToCityId(this@HomeActivity ,sUserData?.locked_seats!![0].to_city!!)
            SharedPreferencesHelper.saveJourneyDate(this@HomeActivity ,sUserData?.locked_seats!![0].journey_date!!)
            SharedPreferencesHelper.saveJourneyTime(this@HomeActivity ,sUserData?.locked_seats!![0].journey_time!!)
            SharedPreferencesHelper.saveJourneyTimeImMillis(this@HomeActivity ,sUserData?.locked_seats!![0].datetime_ms!!)
            SharedPreferencesHelper.saveArrivalTime(this@HomeActivity ,sUserData?.locked_seats!![0].arrival_time)
            SharedPreferencesHelper.saveFlightId(this@HomeActivity ,sUserData?.locked_seats!![0].flight_id!!)

            sUserData?.locked_seats!!.forEach {
                mSeatNamesId.add(it.flight_seat_id!!)
            }
            sUserData?.locked_seats!!.forEach {
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

        val isEntryDone = SharedPreferencesHelper.isEntryLogin(this@HomeActivity)

        if(isEntryDone){
            val displayName = resources.getString(R.string.home_title_hello_again) + sUserData?.name+ ","+
                    resources.getString(R.string.home_title_welcome_back)
            activityHomeBinding.textviewHomeHelloAgain.text = displayName
        }else{
            val displayName = resources.getString(R.string.home_title_hello) + sUserData?.name+ ","+
                    resources.getString(R.string.home_title_welcome)
            activityHomeBinding.textviewHomeHelloAgain.text = displayName
        }


        if(SharedPreferencesHelper.getUserType(this@HomeActivity).equals("primary" , true)){
            activityHomeBinding.textViewHomeBookingFor.visibility = View.GONE
            activityHomeBinding.textViewHomeBookingPrimaryUser.visibility = View.GONE
        } else if(SharedPreferencesHelper.getUserType(this@HomeActivity).equals("secondary" , true)){
            activityHomeBinding.textViewHomeBookingPrimaryUser.text =
                SharedPreferencesHelper.getCurrentPrimaryUserName(this@HomeActivity)
            if(sUserData?.primary_users?.size!! > 1){
                activityHomeBinding.textViewHomeBookingPrimaryUser.setCompoundDrawablesWithIntrinsicBounds(
                    0 , 0 ,R.drawable.ic_user_dropdown,0
                )
            }
            activityHomeBinding.textViewHomeBookingPrimaryUser.setOnClickListener {
                if(sUserData?.primary_users?.size!! > 1){
                    val mHomeIntent = Intent(this@HomeActivity, PrimaryUsersActivity::class.java)
                    // send bundle
                    mHomeIntent.putParcelableArrayListExtra(
                        UIConstants.BUNDLE_SECONDARY_USER_DATA,
                        sUserData?.primary_users as java.util.ArrayList<out Parcelable >
                    )
                    startActivity(mHomeIntent)
                }else{
                    UiUtils.showSimpleDialog(this@HomeActivity ,
                        "You have only one primary user")
                }
            }
        }


        /* launch the Booking flow */
        activityHomeBinding.buttonBookFlight.setOnClickListener {

            val seatsAvailable = SharedPreferencesHelper.getSeatCount(this@HomeActivity)

            if(seatsAvailable == 0) {
                showSimpleDialog(this@HomeActivity, "You don't have seat to Book a Flight, So please purchase now.")
            } else {
                val mPlaceSelectionIntent = Intent(
                    this,
                    PlaceSelectionActivity::class.java
                )
                startActivity(mPlaceSelectionIntent)
            }

        }

        /* launch DrawerActivity */
        activityHomeBinding.buttonDrawer.setOnClickListener {
            val mDrawerActivityIntent = Intent(
                this ,
                DrawerActivity::class.java
            )
//            mPlaceSelectionIntent.putExtra(UIConstants.BUNDLE_USER_DATA , userData)
            startActivity(mDrawerActivityIntent)
        }


    }



    fun showSimpleDialog(context: Context, message :String){
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton("Ok") { _dialog, _ ->
            run {
                _dialog.dismiss()
                startActivity(
                    Intent(this , PurchaseActivity::class.java)
                )
            }
        }
        val simpleDialog = alertDialogBuilder.create()
        simpleDialog.setCanceledOnTouchOutside(false)
        simpleDialog.setCancelable(false)
        simpleDialog.setOnShowListener {
            simpleDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(context,R.color.colorButtonNew))
        }
        simpleDialog.show()
    }

    override fun onResume() {
        super.onResume()
        /* set the seats limit  */
        val seatsAvailable = SharedPreferencesHelper.getSeatCount(this@HomeActivity)
//        val seatsRemaining: SpannableString
        activityHomeBinding.textViewSeatLimits.visibility = View.VISIBLE
        val displaySeats = resources.getString(R.string.home_remaining_seats_first_half) + " " +
                seatsAvailable + " "+
                resources.getString(R.string.home_remaining_seats_second_half)
        activityHomeBinding.textViewSeatLimits.text = displaySeats
        val memberShipType = SharedPreferencesHelper.getMembershipType(this@HomeActivity)
        if(memberShipType.equals(UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO)){
            activityHomeBinding.textViewSeatLimits.visibility = View.GONE
        }
        sendDeviceTokenToServer()
    }

    private fun launchDialog(){
        var lockedFromCity = ""
        var lockedToCity = ""
        sUserData?.cities?.forEach {
            if(it.id == SharedPreferencesHelper.getFromCityId(this)){
                lockedFromCity = it.name
            }
        }
        sUserData?.cities?.forEach {
            if(it.id == SharedPreferencesHelper.getToCityId(this)){
                lockedToCity = it.name
            }
        }
        val numOfSeats = sUserData?.locked_seats!![0].flight?.no_of_seats

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
        alertDialogBuilder.setPositiveButton("Ok") { _, _ ->
            run {
                if (numOfSeats == 8) {
                    val mSeatsIntent = Intent(this@HomeActivity, SeatLayoutOneSelectionActivity::class.java)
                    mSeatsIntent.putExtra("direction", sUserData?.locked_seats!![0].direction)
                    mSeatsIntent.putExtra("sunRiseSet", sUserData?.locked_seats!![0].sun_rise_set)
                    mSeatsIntent.putExtra("flowFrom", "home")
                    startActivity(mSeatsIntent)
                } else if (numOfSeats == 12) {
                    val mSeatsIntent = Intent(this@HomeActivity, SeatSelectionActivity::class.java)
                    mSeatsIntent.putExtra("direction", sUserData?.locked_seats!![0].direction)
                    mSeatsIntent.putExtra("sunRiseSet", sUserData?.locked_seats!![0].sun_rise_set)
                    mSeatsIntent.putExtra("flowFrom", "home")
                    startActivity(mSeatsIntent)
                }else if (numOfSeats == 17) {
                    val mSeatsIntent = Intent(this@HomeActivity, CabinSeatActivity::class.java)
                    mSeatsIntent.putExtra("direction", sUserData?.locked_seats!![0].direction)
                    mSeatsIntent.putExtra("sunRiseSet", sUserData?.locked_seats!![0].sun_rise_set)
                    mSeatsIntent.putExtra("flowFrom", "home")
                    startActivity(mSeatsIntent)
                }

            }
        }

        val stateMaintenenceDialog = alertDialogBuilder.create()
        stateMaintenenceDialog.setCanceledOnTouchOutside(false)
        stateMaintenenceDialog.setCancelable(false)
        stateMaintenenceDialog.setOnShowListener {
            stateMaintenenceDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorButtonNew))
            stateMaintenenceDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorButtonNew))
        }

        stateMaintenenceDialog.show()
    }

    private fun sendDeviceTokenToServer() {

        val mUpdateDeviceTokenCall = RetrofitAPICaller.getInstance(this@HomeActivity)
            .stellarJetAPIs.updateDeviceToken(
            SharedPreferencesHelper.getUserToken(this@HomeActivity),
            SharedPreferencesHelper.getUserType(this@HomeActivity),
            SharedPreferencesHelper.getDeviceToken(this@HomeActivity),
            "android"
        )

        mUpdateDeviceTokenCall.enqueue(object : Callback<UpdateDeviceToken> {
            override fun onResponse(call: Call<UpdateDeviceToken>, response: Response<UpdateDeviceToken>) {
                if (response.body() != null) {
                    Log.d("DeviceToken", "onResponse: " + response.body()!!)
                    if (response.body()!!.resultcode == 1) {
                        Log.d("tokenHome " , SharedPreferencesHelper.getDeviceToken(this@HomeActivity))
                    }
                }else {
                    try {
                        val mJsonObject = JSONObject(response.errorBody()!!.string())
                        val errorMessage = mJsonObject.getString("message")
//                        UiUtils.showSimpleDialog(this@HomeActivity, errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<UpdateDeviceToken>, t: Throwable) {
                Log.d("DeviceToken", "onResponse: $t")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferencesHelper.saveEntryLogin(this@HomeActivity , true)
        mSeatNames.clear()
        mSeatNamesId.clear()
    }
}
