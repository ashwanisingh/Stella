package com.ns.stellarjet.personalize

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ns.networking.model.CabPersonalizeResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityCabPreferncesBinding
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils
import com.ns.stellarjet.utils.UIConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CabPreferencesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCabPreferncesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // obtain binding

        binding = DataBindingUtil.setContentView(
            this ,
            R.layout.activity_cab_prefernces
        )

        binding.editTextPickLocation.setOnClickListener {
            val pickUpIntent = Intent(
                this ,
                SavedAddressActivity::class.java
            )
            pickUpIntent.putExtra(UIConstants.BUNDLE_CAB_TYPE , UIConstants.BUNDLE_CAB_TYPE_PICK)
            startActivity(pickUpIntent)
        }

        binding.editTextDropLocation.setOnClickListener {
            val dropIntent = Intent(
                this ,
                SavedAddressActivity::class.java
            )
            dropIntent.putExtra(UIConstants.BUNDLE_CAB_TYPE , UIConstants.BUNDLE_CAB_TYPE_DROP)
            startActivity(dropIntent)
        }

        binding.buttonCabPreferencesBack.setOnClickListener {
            onBackPressed()
        }

        binding.buttonCabPreferencesUpdate.setOnClickListener {
            if(StellarJetUtils.isConnectingToInternet(applicationContext)){
                updateCabPreferences()
            }else{
                Toast.makeText(applicationContext, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setPersonalizedAddress()
    }

    private fun updateCabPreferences(){
        val personalizeCab : Call<CabPersonalizeResponse> = RetrofitAPICaller.getInstance(this)
            .stellarJetAPIs.personalizeCab(
            SharedPreferencesHelper.getUserToken(this) ,
            SharedPreferencesHelper.getBookingId(this) ,
            SharedPreferencesHelper.getCabPickupPersonlalizeID(this),
            SharedPreferencesHelper.getCabDropPersonalizeID(this)
        )

        personalizeCab.enqueue(object : Callback<CabPersonalizeResponse> {
            override fun onResponse(
                call: Call<CabPersonalizeResponse>,
                response: Response<CabPersonalizeResponse>){
                if (response.body() != null && response.body()!!.resultcode == 1) {
                    Log.d("Booking", "onResponse: " + response.body())
                    SharedPreferencesHelper.saveCabPersonalize(
                        this@CabPreferencesActivity ,
                        true)
                    Toast.makeText(
                        this@CabPreferencesActivity ,
                        response.body()!!.message ,
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<CabPersonalizeResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
            }
        })
    }

    private fun setPersonalizedAddress(){
        val pickupCabPersonalize = SharedPreferencesHelper.getCabPickupPersonlalize(this)
        val dropCabPersonalize = SharedPreferencesHelper.getCabDropPersonalize(this)
        if(!pickupCabPersonalize.isEmpty()){
            binding.editTextPickLocation.text = pickupCabPersonalize
        }
        if(!dropCabPersonalize.isEmpty()){
            binding.editTextDropLocation.text = dropCabPersonalize
        }
    }
}
