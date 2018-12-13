package com.ns.stellarjet.personalize

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ns.stellarjet.R
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.utils.UIConstants
import kotlinx.android.synthetic.main.activity_food_preerences_success.*

class PersonalizeSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_preerences_success)

        button_personalize_success_back.setOnClickListener {
            val mIntent = Intent(
                this,
                HomeActivity::class.java
            )
            mIntent.putExtra(UIConstants.BUNDLE_USER_DATA , HomeActivity.sUserData)
            startActivity(mIntent)
            finish()
        }
    }
}
