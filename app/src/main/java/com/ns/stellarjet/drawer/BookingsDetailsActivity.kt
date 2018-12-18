package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.Booking
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityBookingsDetailsBinding
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.personalize.CabPreferencesActivity
import com.ns.stellarjet.personalize.FoodPreferencesLaunchActivity
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils

class BookingsDetailsActivity : AppCompatActivity() {

    private var isCabPersonalized = false
    private var isFoodPersonalized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityBookingsDetailsBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_bookings_details
        )

        val bookingData: Booking? = intent.extras?.getParcelable("bookingDetails")
        val bookingType = intent.extras?.getString("bookingType")

        if(bookingType.equals("completed" , true)){
            binding.textViewBookingsTitlePersonalize.visibility = View.GONE
            binding.textViewBookingsDetailsPersonalizeTime.visibility = View.GONE
            binding.textViewBookingsDetailsCabsTitle.visibility = View.GONE
            binding.textViewBookingsDetailsFoodTitle.visibility = View.GONE
            binding.view3.visibility = View.GONE
            binding.viewBookingsDetailsDivider.visibility = View.GONE
        }
        binding.bookings = bookingData

        binding.executePendingBindings()

        binding.buttonBookingsDetailsBack.setOnClickListener {
            onBackPressed()
        }

        var passengersName = ""
        var seatsName = ""
        if(bookingData?.travelling_self ==1){
            passengersName = HomeActivity.sUserData.name
            seatsName = bookingData.customer_seat?.seat_code!!

        }else{
            val guests = bookingData?.guest_seats
            guests?.forEach {
                if(passengersName.isEmpty()){
                    passengersName = it.name!!
                    seatsName = it.seat_code!!
                }else{
                    passengersName = passengersName + "," + it.name
                    seatsName = seatsName +"," +it.seat_code
                }
            }
        }

        val expiryTime = getString(R.string.booking_summary_personalize_time_expiry) +
                StellarJetUtils.getPersonalizationHours(bookingData?.journey_datetime!!)
        binding.textViewBookingsDetailsPersonalizeTime.text = expiryTime

        binding.textViewBookingsDetailsPassengers.text = passengersName
        binding.textViewBookingsDetailsSeats.text = seatsName
        binding.textViewBookingsDetailsDate.text = StellarJetUtils.getFormattedBookingsDate(bookingData?.journey_datetime!!)
        val journeyTime = StellarJetUtils.getFormattedhours(bookingData.journey_datetime) + " hrs"
        binding.textViewBookingsDetailsDepartureTime.text = journeyTime
        SharedPreferencesHelper.saveBookingId(this , bookingData.booking_id.toString())

        binding.textViewBookingsDetailsCabsTitle.setOnClickListener {
            startActivity(Intent(this , CabPreferencesActivity::class.java))
        }

        binding.textViewBookingsDetailsFoodTitle.setOnClickListener {
            startActivity(Intent(this , FoodPreferencesLaunchActivity::class.java))
        }

        if(!bookingData.drop_address_main?.isEmpty()!! || !bookingData.pick_address_main?.isEmpty()!!){
            binding.textViewBookingsDetailsCabsTitle.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_tick_ok , 0 ,0 ,0
            )
            isCabPersonalized = true
            SharedPreferencesHelper.saveCabPickupPersoalize(this , bookingData.pick_address)
            SharedPreferencesHelper.saveCabDropPersonalize(this , bookingData.drop_address)
        }else if(bookingData.drop_address_main?.isEmpty()!! && bookingData.pick_address_main?.isEmpty()!!){
            binding.textViewBookingsDetailsCabsTitle.setCompoundDrawablesWithIntrinsicBounds(
                0, 0 ,0 ,0
            )
        }

        if(bookingData.service.equals("usual", true) ){
            binding.textViewBookingsDetailsFoodTitle.setCompoundDrawablesWithIntrinsicBounds(
                0 , 0 ,0 ,0
            )
        }else if(bookingData.service.equals("preferred", true) ){
            binding.textViewBookingsDetailsFoodTitle.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_tick_ok , 0 ,0 ,0
            )
            isFoodPersonalized = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SharedPreferencesHelper.saveCabPickupPersoalize(this , "")
        SharedPreferencesHelper.saveCabDropPersonalize(this , "")
        SharedPreferencesHelper.saveBookingId(this , "")
    }
}
