package com.ns.stellarjet.personalize

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityPersonalizeLaunchBinding

class PersonalizeLaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // obtain binding
        val binding : ActivityPersonalizeLaunchBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_personalize_launch)
        binding.buttonPersonalizeHome.setOnClickListener {
            onBackPressed()
        }

    }
}
