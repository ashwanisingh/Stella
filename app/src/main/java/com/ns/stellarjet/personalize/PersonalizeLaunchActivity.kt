package com.ns.stellarjet.personalize

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_personalize_launch.*

class PersonalizeLaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalize_launch)

        /*val binding : ActivityPersonalizeLaunchBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_personalize_launch)
        binding.*/

        button_personalize_home.setOnClickListener {
            onBackPressed()
        }
    }
}
