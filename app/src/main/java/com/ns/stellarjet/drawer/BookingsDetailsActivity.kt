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
    private var bookingType: String = ""
    private var mSeatCount = 0
    private var mTravellingUsers = 0
    companion object {
        var bookingData: Booking? = null
    }

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
                if(StellarJetUtils.isConnectingToInternet(this@BookingsDetailsActivity)){
                    cancelBooking()
                }else{
                    UiUtils.showNoInternetDialog(this@BookingsDetailsActivity)
                }
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
        val seatIdList : MutableList<Int> = ArrayList()
        if (bookingData?.travelling_self == 1) {
            seatIdList.add(bookingData!!.prefs?.main_passenger?.seats_info?.seat_id!!)
        }else{
            seatIdList.add(bookingData?.prefs?.co_passengers?.get(0)?.seats_info?.seat_id!!)
        }
        val progress = Progress.getInstance()
        progress.showProgress(this@BookingsDetailsActivity)
        val cancelBookingCall: Call<CancelBookingResponse> = RetrofitAPICaller.getInstance(this@BookingsDetailsActivity)
            .stellarJetAPIs.cancelBooking(
            SharedPreferencesHelper.getUserToken(this@BookingsDetailsActivity),
            bookingData?.booking_id!!,
            seatIdList
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

        if(bookingData?.travelling_self == 1){
            Log.d("Cancel Tickets " , bookingData?.prefs?.main_passenger?.name+"")
            Log.d("Cancel Tickets " , bookingData?.prefs?.main_passenger?.seats_info?.seat_code+"")
        }

        val coPassengerSize= bookingData?.prefs?.co_passengers?.size!!
        for (i in 0 until coPassengerSize){
            Log.d("Cancel Tickets " , bookingData?.prefs?.co_passengers?.get(i)?.name)
            Log.d("Cancel Tickets " , bookingData?.prefs?.co_passengers?.get(i)?.seats_info?.seat_code)
        }

        /* set seat count */
        if(bookingData?.travelling_self == 1){
            mSeatCount += 1
        }else{
            mSeatCount = 0
        }

        mSeatCount += bookingData?.prefs?.co_passengers?.size!!

        binding.bookings = bookingData

        binding.executePendingBindings()

        binding.buttonBookingsDetailsBack.setOnClickListener {
            onBackPressed()
        }

        var passengersName = ""
        var seatsName = ""
        if(bookingData?.travelling_self ==1){
            if(bookingData?.prefs?.main_passenger?.status.equals("Confirmed" , true)){
                passengersName = HomeActivity.sUserData.name
                seatsName = bookingData!!.customer_seat?.seat_code!!
                mTravellingUsers += 1
            }else if(bookingType.equals("completed" , true) &&
                bookingData?.prefs?.main_passenger?.status.equals("Cancelled" , true)){
                passengersName = HomeActivity.sUserData.name
                seatsName = bookingData!!.customer_seat?.seat_code!!
                mTravellingUsers += 1
            }
        }
        val guests = bookingData?.prefs!!.co_passengers
        guests?.forEach {
            if(passengersName.isEmpty()){
                if(it.status.equals("Confirmed" , true)){
                    passengersName = it.name!!
                    mTravellingUsers += 1
                }else if(bookingType.equals("completed" , true) &&
                    bookingData?.prefs?.main_passenger?.status.equals("Cancelled" , true)){
                    passengersName = it.name!!
                }
            }else{
                if(it.status.equals("Confirmed" , true)){
                    passengersName = passengersName + ", " + it.name
                    mTravellingUsers += 1
                }else if(bookingType.equals("completed" , true) &&
                    bookingData?.prefs?.main_passenger?.status.equals("Cancelled" , true)){
                    passengersName = passengersName + ", " + it.name
                }
            }
        }
        guests?.forEach {
            if(seatsName.isEmpty()){
                if(it.status.equals("Confirmed" , true)){
                    seatsName = it.seats_info?.seat_code!!
                }else if(bookingType.equals("completed" , true) &&
                    bookingData?.prefs?.main_passenger?.status.equals("Cancelled" , true)){
                    seatsName = it.seats_info?.seat_code!!
                }
            }else{
                if(it.status.equals("Confirmed" , true)){
                    seatsName = seatsName +", " +it.seats_info?.seat_code!!
                }else if(bookingType.equals("completed" , true) &&
                    bookingData?.prefs?.main_passenger?.status.equals("Cancelled" , true)){
                    seatsName = seatsName +", " +it.seats_info?.seat_code!!
                }
            }
        }

        val cities = bookingData?.from_city_info?.name + " to \n"+ bookingData?.to_city_info?.name
        binding.textViewBookingsDetailsFromCity.text = cities

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
            if(mTravellingUsers==1){
                showCancelConfirmation()
//                UiUtils.showToast(this@BookingsDetailsActivity , "1 seats")
            }else{
//                UiUtils.showToast(this@BookingsDetailsActivity , "More then 1 seats")
                val intent  = Intent(this@BookingsDetailsActivity , CancelBookingActivity::class.java)
                intent.putExtra("BookingData" , bookingData)
                startActivity(intent)
            }
        }
        if(bookingType.equals("completed" , true) ||
            bookingData!!.status.equals("Cancelled" , true)){
            binding.buttonBookingsDetailsCancelBooking.visibility = View.GONE
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
            if(bookingData!!.status.equals("Cancelled" , true)){
                binding.imageViewBookingDetailsCancelled.visibility = View.VISIBLE
            }else{
                binding.imageViewBookingDetailsCancelled.visibility = View.GONE
            }
            binding.buttonBookingsDetailsCancelBooking.visibility = View.GONE
            var passengersName = ""
            var seatsName = ""
            if(bookingData?.travelling_self ==1){
                passengersName = HomeActivity.sUserData.name
                seatsName = bookingData!!.customer_seat?.seat_code!!
                mTravellingUsers += 1
            }
            val guests = bookingData?.prefs!!.co_passengers
            guests?.forEach {
                if(passengersName.isEmpty()){
                    if(it.status.equals("Confirmed" , true)){
                        passengersName = it.name!!
                        mTravellingUsers += 1
                    }else if(bookingType.equals("completed" , true) ||
                        it.status.equals("Cancelled" , true)){
                        if(bookingData?.travelling_self == 0 ){
                            passengersName = it.name!!
                        }
                    }
                }else{
                    if(it.status.equals("Confirmed" , true)){
                        passengersName = passengersName + ", " + it.name
                        mTravellingUsers += 1
                    }else if(bookingType.equals("completed" , true) ||
                        it.status.equals("Cancelled" , true)){
                        if(bookingData?.travelling_self == 0 ){
                            passengersName = passengersName + ", " + it.name
                        }
                    }
                }
            }
            guests?.forEach {
                if(seatsName.isEmpty()){
                    if(it.status.equals("Confirmed" , true)){
                        seatsName = it.seats_info?.seat_code!!
                    }else if(bookingType.equals("completed" , true) ||
                        it.status.equals("Cancelled" , true)){
                        if(bookingData?.travelling_self == 0 ){
                            seatsName = it.seats_info?.seat_code!!
                        }
                    }
                }else{
                    if(it.status.equals("Confirmed" , true)){
                        seatsName = seatsName +", " +it.seats_info?.seat_code!!
                    }else if(bookingType.equals("completed" , true) &&
                        it.status.equals("Cancelled" , true)){
                        if(bookingData?.travelling_self == 0 ){
                            seatsName = seatsName +", " +it.seats_info?.seat_code!!
                        }
                    }
                }
            }
            binding.textViewBookingsDetailsPassengers.text = passengersName
            binding.textViewBookingsDetailsSeats.text = seatsName
        }
    }
}
