package com.ns.stellarjet.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_preference_manager_info.*

class PreferenceManagerInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_manager_info)

        button_manager_info_back.setOnClickListener {
            onBackPressed()
        }
    }
}
