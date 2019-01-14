package com.ns.stellarjet.drawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ns.stellarjet.R
import com.ns.stellarjet.personalize.FoodPreferencesLaunchActivity
import kotlinx.android.synthetic.main.activity_preference_launch.*

class PreferenceLaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_launch)

        button_preferences_back.setOnClickListener {
            onBackPressed()
        }

        textView_preferences_managers.setOnClickListener {
            /*startActivity(Intent(
                this ,
                PreferencesManagersListActivity::class.java
            ))*/
            Toast.makeText(this@PreferenceLaunchActivity , "Work in Progress" , Toast.LENGTH_SHORT).show()
        }

        textView_preferences_food.setOnClickListener {
            val FoodPrefencesIntent =  Intent(
                this ,
                FoodPreferencesLaunchActivity::class.java
            )
            FoodPrefencesIntent.putExtra("FlowFrom" , "drawer")
            startActivity(FoodPrefencesIntent)
        }
    }
}
