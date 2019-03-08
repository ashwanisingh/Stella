package com.ns.stellarjet.drawer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.Booking
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityBoardingPassDetailsBinding
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.utils.StellarJetUtils
import kotlinx.android.synthetic.main.activity_boarding_pass_details.*

class BoardingPassDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityBoardingPassDetailsBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_boarding_pass_details
        )

        val boardingPass : Booking? = intent.extras?.getParcelable("BoardingPass")

        binding.boardingPass = boardingPass

        var passengersName = ""
        var seatsName = ""
        var numSeats = 0
        if(boardingPass?.travelling_self ==1){
            passengersName = HomeActivity.sUserData.name
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
        binding.textViewRowBoardingPassDetailsDate.text = StellarJetUtils.getFormattedBookingsDate(boardingPass.journey_datetime)

        val cities = boardingPass.from_city_info?.name + " to \n"+ boardingPass.to_city_info?.name
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
    }
}
