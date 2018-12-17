package com.ns.stellarjet.personalize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivitySavedAddressBinding

class SavedAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // obtain binding
        val binding : ActivitySavedAddressBinding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_saved_address
        )

        binding.textViewSavedAddressCurrentLocation.setOnClickListener {
            startActivity(Intent(
                this ,
                AddAddressScrollActivity::class.java
            ))
        }

        binding.buttonSavedAddressBack.setOnClickListener {
            onBackPressed()
        }

    }
}
