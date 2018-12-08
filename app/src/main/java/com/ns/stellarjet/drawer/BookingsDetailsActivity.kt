package com.ns.stellarjet.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_bookings_details.*

class BookingsDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings_details)

        button_bookings_details_back.setOnClickListener {
            onBackPressed()
        }

    }
}
