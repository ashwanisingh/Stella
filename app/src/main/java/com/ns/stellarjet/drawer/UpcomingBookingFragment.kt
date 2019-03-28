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
import com.ns.stellarjet.databinding.FragmentUpcomingBookingBinding
import com.ns.stellarjet.drawer.adapter.BookingListAdapter
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils
import com.ns.stellarjet.utils.UiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class UpcomingBookingFragment : Fragment(), (Booking , Int) -> Unit {

    private lateinit var binding : FragmentUpcomingBookingBinding
    private var loading = true
    private var pastVisiblesItems: Int = 0
    private var visibleItemCount:Int = 0
    private var totalItemCount:Int = 0
    private var offset = 0
    private var limit = 1000

    private var mLayoutManager: LinearLayoutManager? = null

    companion object {
        var mUpcomingBookingHistoryList: List<Booking> = ArrayList()
        var adapter: BookingListAdapter? = null
    }

    var mUpcomingBookingHistoryList: List<Booking> = ArrayList()
    var adapter: BookingListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,
            com.ns.stellarjet.R.layout.fragment_upcoming_booking, container, false)
        if(StellarJetUtils.isConnectingToInternet(activity)){
            getUpcomingBookings()
        }else{
            context?.let { UiUtils.showNoInternetDialog(it) }
        }

        // Commented By Ashwani
        /*binding.recyclerViewBookingsUpcoming.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                //check for scroll down
                {
                    visibleItemCount = mLayoutManager!!.childCount
                    totalItemCount = mLayoutManager!!.itemCount
                    pastVisiblesItems = mLayoutManager!!.findFirstVisibleItemPosition()

                    if (loading && mUpcomingBookingHistoryList.size==limit) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            //pagination.. i.e. fetch new data
                            offset += 10
                            limit += 10
                            getUpcomingBookings()
                        }
                    }
                }
            }
        })*/
        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getUpcomingBookings() {
        val progress = Progress.getInstance()
        progress.showProgress(activity)
        val upcomingBook: Call<BookingHistoryResponse> = RetrofitAPICaller.getInstance(activity)
            .stellarJetAPIs.getBookingHistoryResponse(
            SharedPreferencesHelper.getUserToken(activity),
            offset,
            limit,
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
                mLayoutManager = LinearLayoutManager(
                    activity ,
                    RecyclerView.VERTICAL ,
                    false
                )
                binding.recyclerViewBookingsUpcoming.adapter = adapter
                binding.recyclerViewBookingsUpcoming.layoutManager = mLayoutManager
                binding.recyclerViewBookingsUpcoming.scrollToPosition(offset)
            }

            override fun onFailure(call: Call<BookingHistoryResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                context?.let { UiUtils.showServerErrorDialog(it) }
            }
        })
    }

    override fun invoke(booking: Booking , position : Int) {
        val mDetailsIntent = Intent(activity , BookingsDetailsActivity::class.java)
        mDetailsIntent.putExtra("bookingDetails" , booking)
        mDetailsIntent.putExtra("selectedBookingPosition" , position)
        mDetailsIntent.putExtra("bookingType" , "upcoming")
        mDetailsIntent.putExtra("from" , "upcoming")
         requireActivity().startActivity(mDetailsIntent)
    }


}// Required empty public constructor


