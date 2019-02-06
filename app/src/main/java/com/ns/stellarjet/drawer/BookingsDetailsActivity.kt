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
import com.ns.stellarjet.personalize.PersonalizeFoodLaunchActivity
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils
import com.ns.stellarjet.utils.UIConstants

class BookingsDetailsActivity : AppCompatActivity() {

    private var isCabPersonalized = false
    private var isFoodPersonalized = false
    private lateinit var binding:ActivityBookingsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_bookings_details
        )

        val bookingData: Booking? = intent.extras?.getParcelable("bookingDetails")
        val bookingType = intent.extras?.getString("bookingType")

        SharedPreferencesHelper.saveFoodPersonalize(
            this@BookingsDetailsActivity,
            false
        )
        SharedPreferencesHelper.saveCabPersonalize(
            this@BookingsDetailsActivity,
            false
        )


        if(bookingType.equals("completed" , true)){
            binding.viewBookingsDetailsDivider.visibility = View.GONE
            binding.layoutBookingsDetailsCabBase.visibility = View.GONE
            binding.layoutBookingsDetailsFoodBase.visibility = View.GONE
            binding.viewCabAfterDivider.visibility = View.GONE
            binding.viewFoodAfterDivider.visibility = View.GONE
            binding.viewDetaisAfterDivider.visibility = View.GONE
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
        }
        val guests = bookingData?.guest_seats
        guests?.forEach {
            if(passengersName.isEmpty()){
                passengersName = it.name!!
                seatsName = it.seat_code!!
            }else{
                passengersName = passengersName + ", " + it.name
                seatsName = seatsName +", " +it.seat_code
            }
        }

        val cities = bookingData?.from_city_info?.name + " to \n"+ bookingData?.to_city_info?.name
        binding.textViewBookingsDetailsFromCity.text = cities

        /*val expiryTime = getString(R.string.booking_summary_personalize_time_expiry) +
                StellarJetUtils.getPersonalizationHours(bookingData.journey_datetime)
        binding.textViewBookingsDetailsPersonalizeTime.text = expiryTime*/

        val bookedBy =  resources.getString(R.string.bookings_booked_by) + " "+bookingData?.booked_by
        binding.textViewBookingDetailsBookedBy.text = bookedBy
        binding.textViewBookingsDetailsPassengers.text = passengersName
        binding.textViewBookingsDetailsSeats.text = seatsName
        binding.textViewBookingsDetailsDate.text = StellarJetUtils.getFormattedBookingsDate(bookingData?.journey_datetime!!)
        val journeyTime = StellarJetUtils.getFormattedhoursInAPM(bookingData.journey_datetime)
        binding.textViewBookingsDetailsDepartureTime.text = journeyTime
        SharedPreferencesHelper.saveBookingId(this , bookingData.booking_id.toString())

        if(bookingData?.prefs?.main_passenger == null){
            binding.viewBookingsDetailsDivider.visibility = View.GONE
            binding.layoutBookingsDetailsCabBase.visibility = View.GONE
            binding.layoutBookingsDetailsFoodBase.visibility = View.GONE
            binding.viewCabAfterDivider.visibility = View.GONE
            binding.viewFoodAfterDivider.visibility = View.GONE
            binding.viewDetaisAfterDivider.visibility = View.GONE
        }

        binding.layoutBookingsDetailsCabBase.setOnClickListener {
            val cabPersonalizeIntent = Intent(this , CabPreferencesActivity::class.java)
            cabPersonalizeIntent.putExtra(UIConstants.BUNDLE_FROM_CITY , bookingData.from_city_info!!.name)
            cabPersonalizeIntent.putExtra(UIConstants.BUNDLE_TO_CITY , bookingData.to_city_info!!.name)
            cabPersonalizeIntent.putExtra("FlowFrom" , "drawer")
            startActivity(cabPersonalizeIntent)
        }

        binding.layoutBookingsDetailsFoodBase.setOnClickListener {
            val foodItems = bookingData?.prefs?.main_passenger?.food_items
            val foodPersonalizeIntent = Intent(this , PersonalizeFoodLaunchActivity::class.java)
//            foodPersonalizeIntent.putExtra("FlowFrom" , "personalize")
            foodPersonalizeIntent.putExtra("JourneyDate" , bookingData.journey_date)
            foodPersonalizeIntent.putExtra("ScheduleId" , bookingData.schedule_id)
            foodPersonalizeIntent.putParcelableArrayListExtra("selectedFoods" , java.util.ArrayList(foodItems))
            startActivity(foodPersonalizeIntent)
        }

        if(!bookingData.drop_address_main?.isEmpty()!! || !bookingData?.pick_address_main?.isEmpty()!!){
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

        if(bookingData.service.equals("standard", true) ){
            binding.textViewBookingsDetailsFoodTitle.setCompoundDrawablesWithIntrinsicBounds(
                0 , 0 ,0 ,0
            )
        }else {
            binding.textViewBookingsDetailsFoodTitle.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_tick_ok , 0 ,0 ,0
            )
            isFoodPersonalized = true
        }
    }

    override fun onRestart() {
        super.onRestart()
        val cabPersonalize = SharedPreferencesHelper.getCabPersonalize(
            this@BookingsDetailsActivity
        )
        val foodPersonalize = SharedPreferencesHelper.getFoodPersonalize(
            this@BookingsDetailsActivity
        )
        if(cabPersonalize){
            binding.textViewBookingsDetailsCabsTitle.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_tick_ok , 0 ,0 ,0
            )
            isCabPersonalized = true
        }
        if(foodPersonalize){
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

    override fun onDestroy() {
        super.onDestroy()
        FoodPreferencesLaunchActivity.mPersonalizationFoodId.clear()
    }
}
