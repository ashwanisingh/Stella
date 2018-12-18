package com.ns.stellarjet.drawer


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Booking
import com.ns.networking.model.BookingHistoryResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.FragmentUpcomingBookingBinding
import com.ns.stellarjet.drawer.adapter.BookingListAdapter
import com.ns.stellarjet.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class UpcomingBookingFragment : Fragment(), (Booking) -> Unit {

    private lateinit var binding : FragmentUpcomingBookingBinding
    private var mUpcomingBookingHistoryList: List<Booking> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_upcoming_booking, container, false)
        getUpcomingBookings()
        return binding.root
    }


    private fun getUpcomingBookings() {
        val upcomingBook: Call<BookingHistoryResponse> = RetrofitAPICaller.getInstance(activity)
            .stellarJetAPIs.getBookingHistoryResponse(
            SharedPreferencesHelper.getUserToken(activity),
            SharedPreferencesHelper.getUserId(activity),
            1,
            20,
            "upcoming"
        )

        upcomingBook.enqueue(object : Callback<BookingHistoryResponse> {
            override fun onResponse(
                call: Call<BookingHistoryResponse>,
                response:
                Response<BookingHistoryResponse>) {
                mUpcomingBookingHistoryList = response.body()!!.data.booking_list
                val adapter = BookingListAdapter(mUpcomingBookingHistoryList ,
                    "Upcoming" ,
                    this@UpcomingBookingFragment)
                val layoutManager = LinearLayoutManager(
                    activity ,
                    RecyclerView.VERTICAL ,
                    false
                )
                binding.recyclerViewBookingsUpcoming.adapter = adapter
                binding.recyclerViewBookingsUpcoming.layoutManager = layoutManager
            }

            override fun onFailure(call: Call<BookingHistoryResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
            }
        })
    }

    override fun invoke(booking: Booking) {
//        Toast.makeText(activity , selectedBooking.flight , Toast.LENGTH_LONG).show()
        val mDetailsIntent = Intent(activity , BookingsDetailsActivity::class.java)
        mDetailsIntent.putExtra("bookingDetails" , booking)
        mDetailsIntent.putExtra("bookingType" , "upcoming")
        requireActivity().startActivity(mDetailsIntent)
    }
}// Required empty public constructor


