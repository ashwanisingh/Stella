package com.ns.stellarjet.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_preference_launch.*

class PreferenceLaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_launch)

        button_preferences_back.setOnClickListener {
            onBackPressed()
        }

        textView_preferences_managers.setOnClickListener {

        }

        textView_preferences_food.setOnClickListener {

        }
    }
}
