package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_drawer.*

class DrawerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        button_drawer_close.setOnClickListener {
            onBackPressed()
        }

        button_drawer_personal_assistance.setOnClickListener {
            // TODO : contact number 180042577777
        }

        textView_drawer_boarding_pass.setOnClickListener {
            //TODO launch Boarding Pass
        }

        textView_drawer_bookings.setOnClickListener {
            startActivity(
                Intent(this , MyBookingsActivity::class.java))
        }

        textView_drawer_preferences.setOnClickListener {
            // TODO launch Preferences
        }
    }
}
