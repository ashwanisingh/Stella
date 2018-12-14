package com.ns.stellarjet.drawer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ns.networking.model.Booking
import com.ns.networking.model.BookingHistoryResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_my_bookings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBookingsActivity : AppCompatActivity() {


    private var mCompletedBookingHistoryList : List<Booking> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        button_bookings_back.setOnClickListener {
            onBackPressed()
        }

        val adapter = BookingsAdapter(supportFragmentManager)
        viewPager_my_bookings.adapter = adapter
    }

    private class BookingsAdapter(fm: FragmentManager?) :
        FragmentStatePagerAdapter(fm) {


        override fun getItem(position: Int): Fragment {
            var mFragment: Fragment? = null
            when (position) {
                0 -> mFragment = UpcomingBookingFragment()
                1 -> mFragment = CompletedBookingsFragment()
            }
            return mFragment!!
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Upcoming"
                1 -> return "Completed"
            }
            return null
        }
    }
}
