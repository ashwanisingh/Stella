package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.Booking
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityBookingsDetailsBinding
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.personalize.CabPreferencesActivity
import com.ns.stellarjet.personalize.FoodPreferenceListActivity
import com.ns.stellarjet.personalize.FoodPreferencesLaunchActivity

class BookingsDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityBookingsDetailsBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_bookings_details
        )

        val bookingData: Booking? = intent.extras?.getParcelable("bookiingDetails")
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

        binding.textViewBookingsDetailsPassengers.text = passengersName
        binding.textViewBookingsDetailsSeats.text = seatsName

        binding.textViewBookingsDetailsCabsTitle.setOnClickListener {
            startActivity(Intent(this , CabPreferencesActivity::class.java))
        }

        binding.textViewBookingsDetailsFoodTitle.setOnClickListener {
            startActivity(Intent(this , FoodPreferencesLaunchActivity::class.java))
        }


    }
}
