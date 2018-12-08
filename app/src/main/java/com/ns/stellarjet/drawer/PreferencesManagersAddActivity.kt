package com.ns.stellarjet.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_preferences_managers_add.*

class PreferencesManagersAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_managers_add)

        button_manager_add_back.setOnClickListener {
            onBackPressed()
        }

        button_manager_add_back.setOnClickListener {
            
        }
    }
}
