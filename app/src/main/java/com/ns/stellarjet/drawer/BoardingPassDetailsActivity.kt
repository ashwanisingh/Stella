package com.ns.stellarjet.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        if(boardingPass?.travelling_self ==1){
            passengersName = HomeActivity.sUserData.name
            seatsName = boardingPass.customer_seat?.seat_code!!
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
        }
        binding.textViewBoardingPassDetailsPassengersName.text = passengersName
        binding.textViewBoardingPassDetailsSeatsName.text = seatsName

        val reachPlaneByHrs = StellarJetUtils.getReachByPlaneHours(boardingPass?.journey_datetime!!) + " hrs"
        binding.textViewBoardingPassDetailsReachPlaneBy.text = reachPlaneByHrs
        val journeyTime = StellarJetUtils.getFormattedhours(boardingPass.journey_datetime) + " hrs"
        binding.textViewBoardingPassDetailsDepartureTime.text = journeyTime
        binding.textViewRowBoardingPassDetailsDate.text = StellarJetUtils.getFormattedBookingsDate(boardingPass.journey_datetime)

        button_boarding_pass_details_back.setOnClickListener {
            onBackPressed()
        }
    }
}
