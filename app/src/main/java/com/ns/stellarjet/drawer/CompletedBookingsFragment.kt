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
import com.ns.stellarjet.utils.StellarJetUtils
import com.ns.stellarjet.utils.UiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class CompletedBookingsFragment : Fragment(), (Booking,Int) -> Unit {

    private var loading = true
    private var pastVisiblesItems: Int = 0
    private var visibleItemCount:Int = 0
    private var totalItemCount:Int = 0
    private var offset = 0
    private var limit = 1000

    private var mLayoutManager: LinearLayoutManager? = null

    private lateinit var binding : FragmentCompletedBookingsBinding
    private var mCompletedBookingHistoryList: List<Booking> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_completed_bookings, container, false)
        if(StellarJetUtils.isConnectingToInternet(this.activity)){
            getCompletedBookings()
        }else{
            context?.let { UiUtils.showSimpleDialog(it, resources.getString(R.string.error_not_connected_internet)) }
        }
        /*binding.recyclerViewBookingsCompleted.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                //check for scroll down
                {
                    visibleItemCount = mLayoutManager!!.childCount
                    totalItemCount = mLayoutManager!!.itemCount
                    pastVisiblesItems = mLayoutManager!!.findFirstVisibleItemPosition()

                    if (loading && mCompletedBookingHistoryList.size==limit) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            //pagination.. i.e. fetch new data
                            offset += 10
                            limit += 10
                            getCompletedBookings()
                        }
                    }
                }
            }
        })*/
        return binding.root
    }

    private fun getCompletedBookings(){
        val upcomingBook : Call<BookingHistoryResponse> = RetrofitAPICaller.getInstance(activity)
            .stellarJetAPIs.getBookingHistoryResponse(
            SharedPreferencesHelper.getUserToken(activity) ,
            offset,
            limit ,
            "completed"
        )

        upcomingBook.enqueue(object : Callback<BookingHistoryResponse> {
            override fun onResponse(
                call: Call<BookingHistoryResponse>,
                response: Response<BookingHistoryResponse>){
                mCompletedBookingHistoryList = response.body()!!.data.booking_list
                val adapter = BookingListAdapter(mCompletedBookingHistoryList ,
                    "Completed",
                    this@CompletedBookingsFragment)
                mLayoutManager = LinearLayoutManager(
                    activity ,
                    RecyclerView.VERTICAL ,
                    false
                )
                binding.recyclerViewBookingsCompleted.adapter = adapter
                binding.recyclerViewBookingsCompleted.layoutManager = mLayoutManager
                binding.recyclerViewBookingsCompleted.scrollToPosition(offset)
            }

            override fun onFailure(call: Call<BookingHistoryResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                context?.let { UiUtils.showSimpleDialog(it, resources.getString(R.string.error_server)) }
            }
        })
    }

    override fun invoke(booking: Booking , position : Int) {
        val mDetailsIntent = Intent(activity , BookingsDetailsActivity::class.java)
        mDetailsIntent.putExtra("bookingDetails" , booking)
        mDetailsIntent.putExtra("bookingType" , "Completed")
        requireActivity().startActivity(mDetailsIntent)
    }

}// Required empty public constructor
