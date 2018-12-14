package com.ns.stellarjet.drawer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Booking
import com.ns.stellarjet.R

class BookingListAdapter(
    private val mBookingList : List<Booking> ,
    private val onSelectDishListenerParams : (Int) -> Unit) : RecyclerView.Adapter<BookingListAdapter.BookingListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_bookings, parent, false)
        return BookingListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mBookingList.size
    }

    override fun onBindViewHolder(holder: BookingListViewHolder, position: Int) {
        holder.bind(mBookingList[position],position , onSelectDishListenerParams)
    }

    class BookingListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val mFromCity : TextView = itemView.findViewById(R.id.textView_bookings_row_from_city)
        private val mToCity : TextView = itemView.findViewById(R.id.textView_bookings_row_to_city)
        private val mFromAirport : TextView = itemView.findViewById(R.id.textView_bookings_airport_from)
        private val mToAirport : TextView = itemView.findViewById(R.id.textView_bookings_airport_to)
        private val mBookingsDate : TextView = itemView.findViewById(R.id.textView_bookings_date)
        private val mBookingsTime: TextView = itemView.findViewById(R.id.textView_bookings_departure_time)
//        private val mPersonalizeStatus: TextView = itemView.findViewById(R.id.textView_booking_personalize_status)

        fun bind(bookings: Booking, position: Int, onSelectDishListenerParams: (Int) -> Unit){
            mFromCity.text = bookings.from_city_info.name
            mToCity.text = bookings.to_city_info.name
            mFromAirport.text = bookings.from_city_info.airport
            mToAirport.text = bookings.to_city_info.airport
            mBookingsDate.text = bookings.journey_date
            mBookingsTime.text = bookings.journey_time
            itemView.setOnClickListener {
                onSelectDishListenerParams(position)
            }
        }
    }

}