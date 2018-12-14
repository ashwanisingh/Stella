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
import com.ns.stellarjet.databinding.FragmentCompletedBookingsBinding
import com.ns.stellarjet.drawer.adapter.BookingListAdapter
import com.ns.stellarjet.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class CompletedBookingsFragment : Fragment(), (Booking) -> Unit {

    private lateinit var binding : FragmentCompletedBookingsBinding
    private var mCompletedBookingHistoryList: List<Booking> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_completed_bookings, container, false)
        getCompletedBookings()
        return binding.root
    }

    private fun getCompletedBookings(){
        val upcomingBook : Call<BookingHistoryResponse> = RetrofitAPICaller.getInstance(activity)
            .stellarJetAPIs.getBookingHistoryResponse(
            SharedPreferencesHelper.getUserToken(activity) ,
            SharedPreferencesHelper.getUserId(activity) ,
            1 ,
            20 ,
            "completed"
        )

        upcomingBook.enqueue(object : Callback<BookingHistoryResponse> {
            override fun onResponse(
                call: Call<BookingHistoryResponse>,
                response: Response<BookingHistoryResponse>){
                mCompletedBookingHistoryList = response.body()!!.data.booking_list
                val adapter = BookingListAdapter(mCompletedBookingHistoryList , this@CompletedBookingsFragment)
                val layoutManager = LinearLayoutManager(
                    activity ,
                    RecyclerView.VERTICAL ,
                    false
                )
                binding.recyclerViewBookingsCompleted.adapter = adapter
                binding.recyclerViewBookingsCompleted.layoutManager = layoutManager
            }

            override fun onFailure(call: Call<BookingHistoryResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
            }
        })
    }

    override fun invoke(booking: Booking) {
        val mDetailsIntent = Intent(activity , BookingsDetailsActivity::class.java)
        mDetailsIntent.putExtra("bookiingDetails" , booking)
        requireActivity().startActivity(mDetailsIntent)
    }

}// Required empty public constructor