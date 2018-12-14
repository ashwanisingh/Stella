package com.ns.stellarjet.drawer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Booking
import com.ns.stellarjet.R
import com.ns.stellarjet.utils.StellarJetUtils

class BoardingListAdapter(
    private val mBoardingPassList : List<Booking>,
    private val onSelectDishListenerParams : (Booking) -> Unit) : RecyclerView.Adapter<BoardingListAdapter.BookingListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_boarding_pass, parent, false)
        return BookingListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mBoardingPassList.size
    }

    override fun onBindViewHolder(holder: BookingListViewHolder, position: Int) {
        holder.bind(mBoardingPassList[position], onSelectDishListenerParams)
    }

    class BookingListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val mFromCity : TextView = itemView.findViewById(R.id.textView_row_boarding_pass_from_city)
        private val mToCity : TextView = itemView.findViewById(R.id.textView_row_boarding_pass_to_city)
        private val mFromAirport : TextView = itemView.findViewById(R.id.textView_row_boarding_pass_from_airport)
        private val mToAirport : TextView = itemView.findViewById(R.id.textView_row_boarding_pass_to_airport)
        private val mBookingsDate : TextView = itemView.findViewById(R.id.textView_row_boarding_pass_date)
        private val mBookingsTime: TextView = itemView.findViewById(R.id.textView_row_boarding_pass_departure_time)
        private val mNoSeats: TextView = itemView.findViewById(R.id.textView_row_boarding_pass_seats)
        private val mFlightName: TextView = itemView.findViewById(R.id.textView_row_boarding_pass_flight_name)
//        private val mPersonalizeStatus: TextView = itemView.findViewById(R.id.textView_booking_personalize_status)

        fun bind(boardingPass: Booking, onSelectDishListenerParams: (Booking) -> Unit){
            mFromCity.text = boardingPass.from_city_info?.name
            mToCity.text = boardingPass.to_city_info?.name
            mFromAirport.text = boardingPass.from_city_info?.airport
            mToAirport.text = boardingPass.to_city_info?.airport
            mBookingsDate.text = StellarJetUtils.getFormattedBookingsDate(boardingPass.journey_datetime)
            val journeyTime = StellarJetUtils.getFormattedhours(boardingPass.journey_datetime) + " hrs"
            mBookingsTime.text = journeyTime
            var numOfSeats = 0
            if(boardingPass.travelling_self == 1){
                numOfSeats += 1
            }
            if(boardingPass.guest_seats!=null){
                numOfSeats += boardingPass.guest_seats?.size!!
            }
            mNoSeats.text = numOfSeats.toString()
            mFlightName.text = boardingPass.flight_no
            itemView.setOnClickListener {
                onSelectDishListenerParams(boardingPass)
            }
        }
    }

}