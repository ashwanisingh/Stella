package com.ns.stellarjet.drawer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager



class BookingsDetailsActivity : AppCompatActivity() {

    private var isCabPersonalized = false
    private var isFoodPersonalized = false
    private lateinit var binding:ActivityBookingsDetailsBinding
    private var bookingType: String? = ""
    private var mSeatCount = 0
    private var mSelectedIndex: Int? = 0
    private var mTravellingUsers = 0
    private var flowFrom: String? = ""
    companion object {
        var bookingData: Booking? = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_bookings_details
        )

        bookingType = intent.extras?.getString("bookingType")
        mSelectedIndex = intent.extras?.getInt("selectedBookingPosition")
        flowFrom = intent.extras?.getString("from")
    }

    override fun onResume() {
        super.onResume()
        if(flowFrom.equals("notifications",true)){
            val bookingId = intent.extras?.getString("bookingId")
            getBookingDetails(bookingId!!)
        }else{
            bookingData = intent.extras?.getParcelable("bookingDetails")!!
//            fillUIData()

            var bookingId :String = bookingData!!.booking_id.toString()

            getBookingDetails(bookingId)
        }
    }

    /*override fun onRestart() {
        super.onRestart()
        val cabPersonalize = SharedPreferencesHelper.getCabPersonalize(
            this@BookingsDetailsActivity
        )
        val foodPersonalize = SharedPreferencesHelper.getFoodPersonalize(
            this@BookingsDetailsActivity
        )
        if(cabPersonalize) {
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
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
//        SharedPreferencesHelper.saveCabPickupPersoalize(this , "")
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
        /*if(1==1) {
            val intent = Intent("TicketCancelBroadcast")
            LocalBroadcastManager.getInstance(this@BookingsDetailsActivity).sendBroadcast(intent)

            finish()
            return
        }*/
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

                // Commented By Ashwani
                // finish()
                if (response.body()!=null){
                    val subscriptionSeats = SharedPreferencesHelper.getSeatCount(this@BookingsDetailsActivity)
                    mSeatCount += subscriptionSeats
                    SharedPreferencesHelper.saveSeatCount(this@BookingsDetailsActivity , mSeatCount)
                    UiUtils.showToast(this@BookingsDetailsActivity , response.message())


                    // Commented By Ashwani
                    /*val selectedIndex = if (mSelectedIndex != null) mSelectedIndex else 0

                    var isSelfTravelling = UpcomingBookingFragment.mUpcomingBookingHistoryList[selectedIndex!!]
                        .travelling_self
                    if(mTravellingUsers == 1 && isSelfTravelling == 1){
                        UpcomingBookingFragment.mUpcomingBookingHistoryList[selectedIndex!!].prefs!!.main_passenger?.
                            status =  "Cancelled"
                    }else if(mTravellingUsers == 1){
                        UpcomingBookingFragment.mUpcomingBookingHistoryList[selectedIndex!!].prefs!!.co_passengers!![0]
                            .status = "Cancelled"
                    }
                    UpcomingBookingFragment.mUpcomingBookingHistoryList[selectedIndex!!].status = "Cancelled"*/


                    // Added By Ashwani
                    killThisActivity();



                }else{
                    UiUtils.showToast(this@BookingsDetailsActivity , "Something went wrong")
                }


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
                passengersName = HomeActivity.sUserData!!.name
                seatsName = bookingData!!.customer_seat?.seat_code!!
                mTravellingUsers += 1
            }else if(bookingType.equals("completed" , true) &&
                bookingData?.prefs?.main_passenger?.status.equals("Cancelled" , true)){
                passengersName = HomeActivity.sUserData!!.name
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
            binding.layoutBookingsDetailsCabBaseUpcoming.visibility = View.GONE
            binding.layoutBookingsDetailsFoodBase.visibility = View.GONE
            binding.viewCabAfterDivider.visibility = View.GONE
            binding.viewFoodAfterDivider.visibility = View.GONE
            binding.viewDetaisAfterDivider.visibility = View.GONE
        }

        if(flowFrom.equals("upcoming")) {
            binding.layoutBookingsDetailsCabBaseUpcoming.setOnClickListener {
                val cabPersonalizeIntent = Intent(this, CabPreferencesActivity::class.java)
                cabPersonalizeIntent.putExtra(UIConstants.BUNDLE_FROM_CITY, bookingData?.prefs?.main_passenger?.pick_address?.address)
                cabPersonalizeIntent.putExtra(UIConstants.BUNDLE_TO_CITY, bookingData?.prefs?.main_passenger?.drop_address?.address)
                cabPersonalizeIntent.putExtra("FlowFrom", "drawer")
                startActivity(cabPersonalizeIntent)
            }
        }

        if(flowFrom.equals("upcoming")) {
            binding.layoutBookingsDetailsFoodBase.setOnClickListener {
                val foodItems = bookingData?.prefs?.main_passenger?.food_items
                val foodPersonalizeIntent = Intent(this, PersonalizeFoodLaunchActivity::class.java)
//            foodPersonalizeIntent.putExtra("FlowFrom" , "personalize")
                foodPersonalizeIntent.putExtra("JourneyDate", bookingData?.journey_date)
                foodPersonalizeIntent.putExtra("ScheduleId", bookingData?.schedule_id)
                foodPersonalizeIntent.putParcelableArrayListExtra("selectedFoods", java.util.ArrayList(foodItems))
                startActivity(foodPersonalizeIntent)
            }
        }

        isCabPersonalized = isCabPersonalise()

        if(isCabPersonalized) {
            binding.textViewBookingsDetailsCabsTitle.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_tick_ok , 0 ,0 ,0
            )
        } else {
            binding.textViewBookingsDetailsCabsTitle.setCompoundDrawablesWithIntrinsicBounds(
                0, 0 ,0 ,0
            )
        }


        isFoodPersonalized = isFoodPersonalise()

        if(isFoodPersonalized) {
            binding.textViewBookingsDetailsFoodTitle.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_tick_ok , 0 ,0 ,0
            )
        } else {
            binding.textViewBookingsDetailsFoodTitle.setCompoundDrawablesWithIntrinsicBounds(
                0 , 0 ,0 ,0
            )
        }

        binding.buttonBookingsDetailsCancelBooking.setOnClickListener {
            if(mTravellingUsers==1){
                showCancelConfirmation()
//                UiUtils.showToast(this@BookingsDetailsActivity , "1 seats")
            }else{
//                UiUtils.showToast(this@BookingsDetailsActivity , "More then 1 seats")
                val intent  = Intent(this@BookingsDetailsActivity , CancelBookingActivity::class.java)
                intent.putExtra("BookingData" , bookingData)
                intent.putExtra("selectedBookingPosition" , mSelectedIndex)
                startActivityForResult(intent, 1000)
            }
        }

        if(flowFrom.equals("completed")) {
            binding.layoutBookingsDetailsCabBaseUpcoming.visibility = View.GONE
            binding.layoutBookingsDetailsCabBaseCompleted.visibility = View.VISIBLE
                    binding.textViewBookingsDetailsCabsTitle1.text = bookingData!!.pick_address_main!!
        binding.textViewBookingsDetailsCabsTitle2.text = bookingData!!.drop_address_main!!
            binding.textViewBookingsDetailsFoodTitleCompleted.visibility = View.VISIBLE
        } else if(flowFrom.equals("upcoming")) {
            binding.layoutBookingsDetailsCabBaseUpcoming.visibility = View.VISIBLE
            binding.layoutBookingsDetailsCabBaseCompleted.visibility = View.GONE
//            binding.textView_bookings_details_food_title_completed =
            binding.textViewBookingsDetailsFoodTitleCompleted.visibility = View.GONE
        }

        // This is executed when user click on item of Completed Tab
        if(bookingType.equals("completed" , true) ||
            bookingData!!.status.equals("Cancelled" , true)) {
            binding.buttonBookingsDetailsCancelBooking.visibility = View.GONE
            var completedAddress = ""
            if(!bookingData!!.pick_address_main!!.isEmpty()){
                completedAddress = bookingData!!.pick_address_main!!
            }

            if(!bookingData!!.drop_address_main!!.isEmpty()){
                completedAddress = if(completedAddress.isEmpty()){
                    bookingData!!.drop_address_main!!
                }else{
                    "\n"+ bookingData!!.drop_address_main
                }
            }

            if(!completedAddress.isEmpty()) {
                binding.textViewBookingsDetailsCabsTitle.text = completedAddress
                binding.imageViewBookingDetailsCabArrow.visibility = View.GONE
            }else{
                binding.layoutBookingsDetailsCabBaseUpcoming.visibility = View.GONE
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
                passengersName = HomeActivity.sUserData!!.name
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

    fun  killThisActivity() {
        val intent = Intent("TicketCancelBroadcast")
        LocalBroadcastManager.getInstance(this@BookingsDetailsActivity).sendBroadcast(intent)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_CANCELED) {
            killThisActivity();
        }
    }


    private fun isCabPersonalise(): Boolean {
        /*if(!bookingData?.drop_address_main?.isEmpty()!! || !bookingData?.pick_address_main?.isEmpty()!!) {
            Log.i("", "")
            return true
        }
        else if(bookingData?.drop_address_main?.isEmpty()!! && bookingData?.pick_address_main?.isEmpty()!!) {
            Log.i("", "")
            return false
        }*/

        if(flowFrom.equals("completed", true)) {
            return false
        }
        else if(bookingData?.prefs?.main_passenger == null) {
            return false
        }
        else if((bookingData?.prefs?.main_passenger?.pick_address == null)
            || bookingData?.prefs?.main_passenger?.drop_address == null) {
            return false
        }
        else if((TextUtils.isEmpty(bookingData?.prefs?.main_passenger?.pick_address?.address_tag))
            || (TextUtils.isEmpty(bookingData?.prefs?.main_passenger?.drop_address?.address_tag)) ) {
            return false
        }

        return true
    }

    private fun isFoodPersonalise() : Boolean {
        if(flowFrom.equals("completed", true)) {
            return false
        }
        else if(bookingData?.service.equals("Usual", true) ) {
            Log.i("", "")
            return false
        }
        else if(bookingData?.service.equals("Preferred", true) ) {
            Log.i("", "")
            return true
        }
        return false
    }


}
