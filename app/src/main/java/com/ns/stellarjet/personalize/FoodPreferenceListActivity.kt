package com.ns.stellarjet.personalize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_food_preference_list.*

class FoodPreferenceListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_preference_list)

        button_food_list_back.setOnClickListener {
            onBackPressed()
        }

        button_food_list_confirm.setOnClickListener {
            finish()
        }
    }
}
