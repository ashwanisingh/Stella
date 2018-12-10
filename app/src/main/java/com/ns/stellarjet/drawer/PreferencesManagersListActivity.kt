package com.ns.stellarjet.drawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ns.stellarjet.R
import kotlinx.android.synthetic.main.activity_preferences_managers_add.*

class PreferencesManagersListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_managers_add)

        button_manager_add_back.setOnClickListener {
//            onBackPressed()
            var managersBottomFragment = ManagersBottomFragment()
            managersBottomFragment.show(supportFragmentManager , managersBottomFragment.tag)
        }

        button_managers_add.setOnClickListener {
            startActivity(Intent(
                this ,
                PreferenceManagerInfoActivity::class.java
            ))
        }
    }
}
