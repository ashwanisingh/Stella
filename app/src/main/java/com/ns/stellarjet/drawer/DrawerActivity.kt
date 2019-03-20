package com.ns.stellarjet.drawer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ns.networking.model.UserData
import com.ns.stellarjet.R
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.utils.PermissionUtils
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants
import com.ns.stellarjet.utils.UiUtils
import kotlinx.android.synthetic.main.activity_drawer.*

class DrawerActivity : AppCompatActivity() {

    private val CALL_PHONE_PERMISSION_REQUEST_CODE = 1

    // Added By Ashwani
    var sUserData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        // Added By Ashwani
        sUserData = SharedPreferencesHelper.getUserData(this)

        button_drawer_close.setOnClickListener {
            onBackPressed()
        }

        button_drawer_personal_assistance.setOnClickListener {
            // TODO : contact number 180042577777

            val customerCare = sUserData?.customer_care_info?.phone!!
            if (ContextCompat.checkSelfPermission(this@DrawerActivity, Manifest.permission.CALL_PHONE) !== PackageManager.PERMISSION_GRANTED) {
                // Permission to access the location is missing.
                PermissionUtils.requestPhonePermission(this ,
                    CALL_PHONE_PERMISSION_REQUEST_CODE ,
                    Manifest.permission.CALL_PHONE ,
                    false)
                /*PermissionUtils.requestPhonePermission(
                    this@DrawerActivity, CALL_PHONE_PERMISSION_REQUEST_CODE,
                    Manifest.permission.CALL_PHONE, true)*/

            } else {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$customerCare")//change the number
                startActivity(callIntent)
            }

        }

        textView_drawer_boarding_pass.setOnClickListener {
            val mBoardingPassIntent = Intent(
                this ,
                BoardingPassActivity::class.java
            )
            startActivity(mBoardingPassIntent)
        }

        textView_drawer_bookings.setOnClickListener {
            startActivity(
                Intent(this , MyBookingsActivity::class.java))
        }

        textView_drawer_preferences.setOnClickListener {
            startActivity(
                Intent(this , PreferenceLaunchActivity::class.java)
            )
        }

        val userType = SharedPreferencesHelper.getUserType(this@DrawerActivity)
        if(userType.equals("secondary" , true)){
            textView_drawer_buy_seats.visibility = View.INVISIBLE
            view_buy_seats.visibility = View.INVISIBLE
        }
        val memberShipType = SharedPreferencesHelper.getMembershipType(this@DrawerActivity)
        if(memberShipType.equals(UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO, true)){
            textView_drawer_buy_seats.visibility = View.INVISIBLE
            view_buy_seats.visibility = View.INVISIBLE
        }

        textView_drawer_buy_seats.setOnClickListener {
            startActivity(
                Intent(this , PurchaseActivity::class.java)
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray ) {
        if (requestCode != CALL_PHONE_PERMISSION_REQUEST_CODE) {
            return
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,Manifest.permission.CALL_PHONE)) {
            // Call to customerCare.
            val customerCare = sUserData?.customer_care_info?.phone!!
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$customerCare")//change the number
            startActivity(callIntent)
        } else {
            // Display the missing permission error dialog when the fragments resume.
            UiUtils.showSimpleDialog(this@DrawerActivity , "Permission Denied")
            /*PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(supportFragmentManager, "dialog")*/
        }
    }
}
