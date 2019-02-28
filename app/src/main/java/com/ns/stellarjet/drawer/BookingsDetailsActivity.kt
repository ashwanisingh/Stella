package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.Booking
import com.ns.networking.model.BookingDetailsResponse
import com.ns.networking.model.CancelBookingResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityBookingsDetailsBinding
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.personalize.CabPreferencesActivity
import com.ns.stellarjet.personalize.FoodPreferencesLaunchActivity
import com.ns.stellarjet.personalize.PersonalizeFoodLaunchActivity
import com.ns.stellarjet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingsDetailsActivity : AppCompatActivity() {

    private var isCabPersonalized = false
    private var isFoodPersonalized = false
    private lateinit var binding:ActivityBookingsDetailsBinding
    private var bookingData: Booking? = null
    private var bookingType: String = ""
    private var mSeatCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_bookings_details
        )

        bookingType = intent.extras?.getString("bookingType")!!
        val from = intent.extras?.getString("from")
        if(from.equals("notifications",true)){
            val bookingId = intent.extras?.getString("bookingId")
            getBookingDetails(bookingId!!)
        }else{
            bookingData = intent.extras?.getParcelable("bookingDetails")!!
            fillUIData()
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


    private fun showCancelConfirmation(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Cancel booking")
        alertDialogBuilder.setMessage("Do you want to cancel this booking ?" )
        alertDialogBuilder.setPositiveButton("Ok") { _, _ ->
            run {
                //                UiUtils.showToast(this@BookingsDetailsActivity , "Cancel ok")
                cancelBooking()
            }
        }

        alertDialogBuilder.setNegativeButton("Cancel") { _dialog, _ ->
            run {
                _dialog.dismiss()
            }
        }

        val cancelBookingDialog = alertDialogBuilder.create()
        cancelBookingDialog.setCanceledOnTouchOutside(false)
        cancelBookingDialog.setCancelable(false)
        cancelBookingDialog.setOnShowListener {
            cancelBookingDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorButtonNew))
            cancelBookingDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorButtonNew))
        }

        cancelBookingDialog.show()
    }

    private fun cancelBooking(){
        val progress = Progress.getInstance()
        progress.showProgress(this@BookingsDetailsActivity)
        val cancelBookingCall: Call<CancelBookingResponse> = RetrofitAPICaller.getInstance(this@BookingsDetailsActivity)
            .stellarJetAPIs.cancelBooking(
            SharedPreferencesHelper.getUserToken(this@BookingsDetailsActivity),
            bookingData?.booking_id!!
        )

        cancelBookingCall.enqueue(object : Callback<CancelBookingResponse> {
            override fun onResponse(
                call: Call<CancelBookingResponse>,
                response:
                Response<CancelBookingResponse>) {
                progress.hideProgress()
                var subscriptionSeats = SharedPreferencesHelper.getSeatCount(this@BookingsDetailsActivity)
                mSeatCount += subscriptionSeats
                SharedPreferencesHelper.saveSeatCount(this@BookingsDetailsActivity , mSeatCount)
                UiUtils.showToast(this@BookingsDetailsActivity , "Booking Canceled")
                finish()
                /*if (response.body()!=null){
                    UiUtils.showToast(this@BookingsDetailsActivity , response.message())
                    finish()
                }else{
//                    UiUtils.showToast(this@BookingsDetailsActivity , "Something went wrong")
                }*/
            }

            override fun onFailure(call: Call<CancelBookingResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                UiUtils.showServerErrorDialog(this@BookingsDetailsActivity)
            }
        })
    }

    private fun getBookingDetails(bookingId : String){
        val progress = Progress.getInstance()
        progress.showProgress(this@BookingsDetailsActivity)
        val bookingDetailsCall: Call<BookingDetailsResponse> = RetrofitAPICaller.getInstance(this@BookingsDetailsActivity)
            .stellarJetAPIs.getBookingDetails(
            SharedPreferencesHelper.getUserToken(this@BookingsDetailsActivity),
            bookingId
        )

        bookingDetailsCall.enqueue(object : Callback<BookingDetailsResponse> {
            override fun onResponse(
                call: Call<BookingDetailsResponse>,
                response:
                Response<BookingDetailsResponse>) {
                progress.hideProgress()
                Log.d("Booking",response.body().toString())
                bookingData = response.body()?.data?.details?.get(0)
                fillUIData()
            }

            override fun onFailure(call: Call<BookingDetailsResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                UiUtils.showServerErrorDialog(this@BookingsDetailsActivity)
            }
        })
    }

    private fun fillUIData(){
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

        /* set seat count */
        if(bookingData?.travelling_self == 1){
            mSeatCount += 1
        }else{
            mSeatCount = 0
        }

        mSeatCount += bookingData?.prefs?.co_passengers?.size!!

        if(bookingData!!.status.equals("Cancelled" , true)){
            var address = ""
            if(!bookingData!!.pick_address_main!!.isEmpty()){
                address = bookingData!!.pick_address_main!!
            }

            if(!bookingData!!.drop_address_main!!.isEmpty()){
                address = if(address.isEmpty()){
                    bookingData!!.drop_address_main!!
                }else{
                    "\n"+ bookingData!!.drop_address_main
                }
            }


            if(!address.isEmpty()){
                binding.textViewBookingsDetailsCabsTitle.text = address
                binding.imageViewBookingDetailsCabArrow.visibility = View.GONE
            }else{
                binding.layoutBookingsDetailsCabBase.visibility = View.GONE
                binding.viewCabAfterDivider.visibility = View.GONE
            }
            if(bookingData?.service.equals("Usual", true) ){
                binding.layoutBookingsDetailsFoodBase.visibility = View.GONE
                binding.viewFoodAfterDivider.visibility = View.GONE
                binding.viewDetaisAfterDivider.visibility = View.GONE
            }else{
                var foodsSelected = ""
                bookingData?.prefs?.main_passenger?.food_items?.forEach {
                    foodsSelected = if(foodsSelected.isEmpty()){
                        it.name!!
                    }else{
                        foodsSelected + ","+it.name!!
                    }
                }
                if(foodsSelected.isNotEmpty()){
                    binding.textViewBookingsDetailsFoodTitle.text = foodsSelected
                    binding.imageViewBookingDetailsDiningArrow.visibility = View.GONE
                }
            }
//            binding.imageViewBookingDetailsDiningArrow.visibility = View.GONE
            binding.imageViewBookingDetailsCancelled.visibility = View.VISIBLE
            binding.buttonBookingsDetailsCancelBooking.visibility = View.GONE
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
            seatsName = bookingData!!.customer_seat?.seat_code!!
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
        val journeyTime = StellarJetUtils.getFormattedhoursInAPM(bookingData!!.journey_datetime)
        binding.textViewBookingsDetailsDepartureTime.text = journeyTime
        SharedPreferencesHelper.saveBookingId(this , bookingData!!.booking_id.toString())

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
            cabPersonalizeIntent.putExtra(UIConstants.BUNDLE_FROM_CITY , bookingData?.from_city_info!!.name)
            cabPersonalizeIntent.putExtra(UIConstants.BUNDLE_TO_CITY , bookingData?.to_city_info!!.name)
            cabPersonalizeIntent.putExtra("FlowFrom" , "drawer")
            startActivity(cabPersonalizeIntent)
        }

        binding.layoutBookingsDetailsFoodBase.setOnClickListener {
            val foodItems = bookingData?.prefs?.main_passenger?.food_items
            val foodPersonalizeIntent = Intent(this , PersonalizeFoodLaunchActivity::class.java)
//            foodPersonalizeIntent.putExtra("FlowFrom" , "personalize")
            foodPersonalizeIntent.putExtra("JourneyDate" , bookingData?.journey_date)
            foodPersonalizeIntent.putExtra("ScheduleId" , bookingData?.schedule_id)
            foodPersonalizeIntent.putParcelableArrayListExtra("selectedFoods" , java.util.ArrayList(foodItems))
            startActivity(foodPersonalizeIntent)
        }

        if(!bookingData?.drop_address_main?.isEmpty()!! || !bookingData?.pick_address_main?.isEmpty()!!){
            binding.textViewBookingsDetailsCabsTitle.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_tick_ok , 0 ,0 ,0
            )
            isCabPersonalized = true
            SharedPreferencesHelper.saveCabPickupPersoalize(this , bookingData?.pick_address)
            SharedPreferencesHelper.saveCabDropPersonalize(this , bookingData?.drop_address)
        }else if(bookingData?.drop_address_main?.isEmpty()!! && bookingData?.pick_address_main?.isEmpty()!!){
            binding.textViewBookingsDetailsCabsTitle.setCompoundDrawablesWithIntrinsicBounds(
                0, 0 ,0 ,0
            )
        }

        if(bookingData?.service.equals("Usual", true) ){
            binding.textViewBookingsDetailsFoodTitle.setCompoundDrawablesWithIntrinsicBounds(
                0 , 0 ,0 ,0
            )
        }else {
            binding.textViewBookingsDetailsFoodTitle.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_tick_ok , 0 ,0 ,0
            )
            isFoodPersonalized = true
        }
        binding.buttonBookingsDetailsCancelBooking.setOnClickListener {
            showCancelConfirmation()
        }
    }
}
