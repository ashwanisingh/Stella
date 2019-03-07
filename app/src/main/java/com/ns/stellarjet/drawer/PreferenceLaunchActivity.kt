package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ns.stellarjet.R
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants
import kotlinx.android.synthetic.main.activity_preference_launch.*

class PreferenceLaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_launch)

        button_preferences_back.setOnClickListener {
            onBackPressed()
        }

        val userType = SharedPreferencesHelper.getUserType(this@PreferenceLaunchActivity)
        val memberShipType = SharedPreferencesHelper.getMembershipType(this@PreferenceLaunchActivity)
        if(userType.equals("secondary" , true)){
            layout_preferences_managers.visibility = View.INVISIBLE
            textView_preferences_managers.visibility = View.INVISIBLE
            textView_preferences_managers_help.visibility = View.INVISIBLE
            view_preferences_managers.visibility = View.INVISIBLE
        }
        if(memberShipType.equals(UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO ,true)){
            layout_preferences_managers.visibility = View.INVISIBLE
            textView_preferences_managers.visibility = View.INVISIBLE
            textView_preferences_managers_help.visibility = View.INVISIBLE
            view_preferences_managers.visibility = View.INVISIBLE
        }

        layout_preferences_managers.setOnClickListener {
            startActivity(Intent(
                this ,
                PreferencesManagersListActivity::class.java
            ))
        }

        layout_preferences_food.setOnClickListener {
            val foodPreferencesIntent =  Intent(
                this ,
                GlobalFoodSelectionActivity::class.java
            )
            foodPreferencesIntent.putExtra("FlowFrom" , "drawer")
            startActivity(foodPreferencesIntent)
        }
    }
}
