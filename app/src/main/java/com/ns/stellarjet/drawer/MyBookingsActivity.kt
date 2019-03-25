package com.ns.stellarjet.drawer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_my_bookings.*
import com.google.android.material.tabs.TabLayout

class MyBookingsActivity : AppCompatActivity() {

    private var mCancelReceiver: CancelReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        button_bookings_back.setOnClickListener {
            onBackPressed()
        }

        val adapter = BookingsAdapter(supportFragmentManager)
        viewPager_my_bookings.adapter = adapter
        val tabLayout = findViewById<View>(R.id.sliding_tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager_my_bookings)

        // Added By Ashwani
        mCancelReceiver = CancelReceiver()
        // Registering Local Broadcast Receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mCancelReceiver!!,
            IntentFilter("TicketCancelBroadcast")
        )
    }

    private class BookingsAdapter(fm: FragmentManager?) :
        FragmentStatePagerAdapter(fm!!) {


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

    private inner class CancelReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            startActivity(
                Intent(this@MyBookingsActivity , MyBookingsActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mCancelReceiver!!)
        super.onDestroy()
    }
}
