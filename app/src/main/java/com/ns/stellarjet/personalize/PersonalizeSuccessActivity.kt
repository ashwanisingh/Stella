package com.ns.stellarjet.personalize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ns.stellarjet.R
import com.ns.stellarjet.home.HomeActivity
import kotlinx.android.synthetic.main.activity_food_preerences_success.*

class PersonalizeSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_preerences_success)

        button_personalize_success_back.setOnClickListener {
            startActivity(Intent(
                this , HomeActivity::class.java
            ))
            finish()
        }
    }
}
