package com.ns.stellarjet.drawer


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class UpcomingBookingFragment : Fragment(), (Booking) -> Unit {

    private lateinit var binding : FragmentUpcomingBookingBinding
    companion object {
        var mUpcomingBookingHistoryList: List<Booking> = ArrayList()
        var adapter: BookingListAdapter? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_upcoming_booking, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(StellarJetUtils.isConnectingToInternet(activity)){
            getUpcomingBookings()
        }else{
            Toast.makeText(activity, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUpcomingBookings() {
        val progress = Progress.getInstance()
        progress.showProgress(activity)
        val upcomingBook: Call<BookingHistoryResponse> = RetrofitAPICaller.getInstance(activity)
            .stellarJetAPIs.getBookingHistoryResponse(
            SharedPreferencesHelper.getUserToken(activity),
            1,
            150,
            "upcoming"
        )

        upcomingBook.enqueue(object : Callback<BookingHistoryResponse> {
            override fun onResponse(
                call: Call<BookingHistoryResponse>,
                response:
                Response<BookingHistoryResponse>) {
                progress.hideProgress()
                mUpcomingBookingHistoryList = response.body()!!.data.booking_list
                adapter = BookingListAdapter(mUpcomingBookingHistoryList ,
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
                progress.hideProgress()
                Toast.makeText(activity , "Server Error" , Toast.LENGTH_SHORT).show()
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


