package com.ns.stellarjet.drawer

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.Booking
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityBoardingPassDetailsBinding
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.utils.*
import kotlinx.android.synthetic.main.activity_boarding_pass_details.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardingPassDetailsActivity : AppCompatActivity(), TermsConditionPanel.TCSliderListener {

    var tcPanel: TermsConditionPanel? = null
    var cities: String? = null
    var citiesForFileName: String? = null
    var date: String? = null
    var boardingPassUrl: String? = null

    override fun onTcSliderVisibilityChanged(visibility: Int) {


    }

    override fun onTCButtonClick(isUserAgree: Boolean) {
        if(isUserAgree) {
            boardingPassUrl = "https://images.pexels.com/photos/414612/pexels-photo-414612.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500";
            tcPanel!!.hideTcSlider();
            download(boardingPassUrl!!, citiesForFileName!!, date!!)
        } else {

        }

    }

    override fun onTCSliderSlide(percentage: Float) {
        button_boarding_pass_details_back.alpha = 1-((100-percentage) / 100);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityBoardingPassDetailsBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_boarding_pass_details
        )

        // Bottom to Top Slider Wrapper Initialisation
        tcPanel = TermsConditionPanel(this, this, "BOARDING")

        val boardingPass : Booking? = intent.extras?.getParcelable("BoardingPass")

        binding.boardingPass = boardingPass

        var passengersName = ""
        var seatsName = ""
        var numSeats = 0
        if(boardingPass?.travelling_self ==1){
            passengersName = HomeActivity.sUserData?.name!!
            seatsName = boardingPass.customer_seat?.seat_code!!
            numSeats += 1
        }
        val guests = boardingPass?.guest_seats
        guests?.forEach {
            if(passengersName.isEmpty()){
                passengersName = it.name!!
                seatsName = it.seat_code!!
            }else{
                passengersName = passengersName + ", " + it.name
                seatsName = seatsName +", " +it.seat_code
            }
            numSeats += 1
        }
        /*val passengerTitle = resources.getString(R.string.booking_summary_passengers) +" ("+ numSeats +")"
        binding.textViewBoardingPassDetailsPassengersTitle.text = passengerTitle*/
        binding.textViewBoardingPassDetailsPassengersName.text = passengersName
        binding.textViewBoardingPassDetailsSeatsName.text = seatsName

        val reachPlaneByHrs = StellarJetUtils.getReachByPlaneHours(boardingPass?.journey_datetime!!)
        val reachPaneByHrs = resources.getString(R.string.boarding_passengers_reach_by)+" "+reachPlaneByHrs
        binding.textViewBoardingPassDetailsReachPlaneBy.text = reachPaneByHrs
        val journeyTime = StellarJetUtils.getFormattedhoursInAPM(boardingPass.journey_datetime)
        binding.textViewBoardingPassDetailsDepartureTime.text = journeyTime
        date = StellarJetUtils.getFormattedBookingsDate(boardingPass.journey_datetime)
        binding.textViewRowBoardingPassDetailsDate.text = date

        cities = boardingPass.from_city_info?.name + " to \n"+ boardingPass.to_city_info?.name
        citiesForFileName = boardingPass.from_city_info?.name + " to "+ boardingPass.to_city_info?.name
        binding.textViewRowBoardingPassDetailsFromCity.text = cities

        var foodNames = ""
        if(boardingPass.travelling_self ==1 ){
            boardingPass.prefs?.main_passenger?.food_items?.forEach {
                if(foodNames.isEmpty()){
                    foodNames = it.name!!
                }else{
                    foodNames =  foodNames + " , "+ it.name
                }
            }
            binding.textViewBoardingPassDetailsFoodsNo.text = foodNames
        }else{
            binding.textViewBoardingPassDetailsFoodsNo.text = "N/A"
        }
        if(foodNames.isEmpty()){
            binding.textViewBoardingPassDetailsFoodsNo.text = "N/A"
        }
        if(journeyTime.contains("pm" , true)){
            binding.textViewBoardingPassDetailsFoods.text = resources.getString(R.string.boarding_passengers_dinner)
        }

        button_boarding_pass_details_back.setOnClickListener {
            onBackPressed()
        }


        binding.buttonDownload.setOnClickListener {
//            ToastUtils.showToast(this, "Under Progress.")

            var isAllowed = true;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isAllowed = checkLocationPermission()
            }

            if(isAllowed) {
                tcPanel?.showTcSlider()
            }

        }

        var textCount = 0
        binding.textViewRowBoardingPassDetailsFromCity.setOnClickListener(View.OnClickListener {
            textCount++;

            if(textCount == 11) {
                var input = EditText(this@BoardingPassDetailsActivity)
                var lp =  LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                var alert = AlertDialog.Builder(this@BoardingPassDetailsActivity)
                alert.setView(input);

                input.setText(SharedPreferencesHelper.getUserToken(this@BoardingPassDetailsActivity))
                alert.show();
                textCount = 0;
            }
        })



    }

    override fun onBackPressed() {
        if(tcPanel?.isVisible() == true) {
            tcPanel?.hideTcSlider();
            return;
        }

        super.onBackPressed()
    }


    /////////////////////////////////////////

    private val MY_PERMISSIONS_REQUEST_SDCARD = 99

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_SDCARD
                )


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_SDCARD
                )
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SDCARD -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                    finish()
                }
                return
            }
        }// other 'case' lines to check for other permissions this app might request.
        // You can add here other case statements according to your requirement.
    }



    fun download(url: String, sourceToDestination: String, date: String  ) {

        val progress = Progress.getInstance()
        progress.showProgress(this@BoardingPassDetailsActivity)

        val call: Call<ResponseBody> = RetrofitAPICaller.getInstance(this@BoardingPassDetailsActivity).stellarJetAPIs.fetchCaptcha(url)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it


                        var isDownloaded = StellarJetUtils.DownloadImage(response.body(),
                            this@BoardingPassDetailsActivity, sourceToDestination+" "+date)
                        Log.i("", "")

                        UiUtils.showToast(
                            this@BoardingPassDetailsActivity,
                            "Saved successfully in Gallery"
                        )

                    } else {
                        // TODO
                        Log.i("", "")
                    }
                } else {
                    // TODO
                    Log.i("", "")
                }

                progress.hideProgress()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // TODO
                Log.i("", "")
                progress.hideProgress()
                UiUtils.showToast(
                    this@BoardingPassDetailsActivity,
                    "Please try again later"
                )
            }
        })





    }




}
