package com.ns.stellarjet.drawer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Booking
import com.ns.stellarjet.R
import com.ns.stellarjet.utils.StellarJetUtils

class BookingListAdapter(
    private val mBookingList : List<Booking> , val bookingType :String ,
    private val onSelectDishListenerParams : (Booking , Int) -> Unit) : RecyclerView.Adapter<BookingListAdapter.BookingListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_bookings, parent, false)
        return BookingListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mBookingList.size
    }

    override fun onBindViewHolder(holder: BookingListViewHolder, position: Int) {
        holder.bind(mBookingList[position], bookingType , onSelectDishListenerParams , position)
    }

    class BookingListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val mFromCity : TextView = itemView.findViewById(R.id.textView_bookings_row_from_city)
        private val mToCity : TextView = itemView.findViewById(R.id.textView_bookings_row_to_city)
        private val mFromAirport : TextView = itemView.findViewById(R.id.textView_bookings_airport_from)
        private val mToAirport : TextView = itemView.findViewById(R.id.textView_bookings_airport_to)
        private val mBookingsDate : TextView = itemView.findViewById(R.id.textView_bookings_date)
        private val mBookingsTime: TextView = itemView.findViewById(R.id.textView_bookings_departure_time)
        private val mPersonalizeStatus: TextView = itemView.findViewById(R.id.textView_booking_personalize_status)
        private val mDivider: View = itemView.findViewById(R.id.view_bookings_two)
        private val mCancelImageView: ImageView = itemView.findViewById(R.id.imageView_booking_cancelled)


        fun bind(
            bookings: Booking,
            bookingType: String,
            onSelectDishListenerParams: (Booking, Int) -> Unit,
            position: Int
        ){
            mFromCity.text = bookings.from_city_info?.name
            mToCity.text = bookings.to_city_info?.name
            mFromAirport.text = bookings.from_city_info?.airport
            mToAirport.text = bookings.to_city_info?.airport
            mBookingsDate.text = StellarJetUtils.getFormattedCompeltedDate(bookings.journey_datetime)
            val journeyTime = StellarJetUtils.getFormattedhours(bookings.journey_datetime) + " hrs"
            mBookingsTime.text = journeyTime
            itemView.setOnClickListener {
                onSelectDishListenerParams(bookings , position)
            }

            if(bookings.status.equals("Cancelled" , true)){
                mCancelImageView.visibility = View.VISIBLE
            } else {
                mCancelImageView.visibility = View.GONE
            }

            if(bookingType.equals("Completed" , true)){
                mDivider.visibility = View.GONE
                mPersonalizeStatus.visibility = View.GONE
            } else {
                mDivider.visibility = View.VISIBLE
                mPersonalizeStatus.visibility = View.VISIBLE
            }

            if(bookings.prefs?.main_passenger == null){
                mDivider.visibility = View.GONE
                mPersonalizeStatus.visibility = View.GONE
            }

            val pickUpAddress = bookings.pick_address_main
            val dropAddress = bookings.drop_address_main
            var isCabPersonalized = false
            val isFoodPersonalized: Boolean = !bookings.service.equals("Usual", true)
            if(pickUpAddress?.isEmpty()!! && dropAddress?.isEmpty()!!){
                isCabPersonalized = false
            }else if(pickUpAddress.isEmpty().not() || dropAddress?.isEmpty()?.not()!!){
                isCabPersonalized = true
            }

            if(isCabPersonalized && isFoodPersonalized){
                mPersonalizeStatus.text = itemView.context.getString(R.string.booking_summary_done_personalize)
                mPersonalizeStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.mipmap.ic_booking_done, 0 , 0 , 0
                )
//                mPersonalizeStatus.setTextColor(itemView.context.resources.getColor(R.color.colorBookingsPersonalize))
                mPersonalizeStatus.setTextColor(ContextCompat.getColor(itemView.context , R.color.colorBookingsPersonalize))
            } else {
                if(bookings.status.equals("Cancelled" , true)) {
                    mPersonalizeStatus.text = itemView.context.getString(R.string.booking_summary_cancel_ticket)
                } else {
                    mPersonalizeStatus.text = itemView.context.getString(R.string.please_personalize_your_preferences)
                }
                mPersonalizeStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_info_personalize , 0 , 0 , 0
                )
//                mPersonalizeStatus.setTextColor(itemView.context.resources.getColor(R.color.colorBookingsPersonalize))
                mPersonalizeStatus.setTextColor(ContextCompat.getColor(itemView.context ,R.color.colorBookingsPersonalize))
            }


        }
    }

}